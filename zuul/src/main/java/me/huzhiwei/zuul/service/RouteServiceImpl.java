package me.huzhiwei.zuul.service;

import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.domain.Route;
import me.huzhiwei.zuul.domain.RouteGroup;
import me.huzhiwei.zuul.exception.BusinessException;
import me.huzhiwei.zuul.mapper.RouteGroupMapper;
import me.huzhiwei.zuul.mapper.RouteMapper;
import me.huzhiwei.zuul.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@Transactional
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteGroupMapper routeGroupMapper;
    @Autowired
    private RouteMapper routeMapper;

    /**
     * 目前生效的route table
     * name -> ZuulRoute
     */
    private static Map<String, Map<String, ZuulProperties.ZuulRoute>> routesMap;

    @PostConstruct
    public void init() {
        loadAllRoutes();
    }

    private void loadAllRoutes() {
        //清空routesMap
        routesMap = new ConcurrentHashMap<>();

        List<Route> allRoutes = routeMapper.selectAll();
        ZuulProperties.ZuulRoute zuulRoute;
        for (Route route : allRoutes) {
            zuulRoute = new ZuulProperties.ZuulRoute();
            BeanUtils.copyProperties(route, zuulRoute);

            if (!routesMap.containsKey(route.getGroupId())) {
                routesMap.put(route.getGroupId(), new ConcurrentHashMap<>());
            }
            routesMap.get(route.getGroupId()).put(route.getId(), zuulRoute);
        }
    }

    @Override
    public void deleteRouteGroup(String id) {
    	routeGroupMapper.delete(new RouteGroup(id));
    	Route delete = new Route();
    	delete.setGroupId(id);
    	routeMapper.delete(delete);
        routesMap.remove(id);
    }

    @Override
    public void addRouteGroup(RouteGroup routeGroup) throws BusinessException {
    	RouteGroup query = new RouteGroup();
    	query.setName(routeGroup.getName());
        if (!CollectionUtils.isEmpty(routeGroupMapper.select(query))) {
            throw new BusinessException(String.format("group: {%s} already exists.", routeGroup));
        }
        routeGroupMapper.insert(routeGroup);
        routesMap.put(routeGroup.getId(), new ConcurrentHashMap<>());
    }

    @Override
    public void updateRouteGroup(RouteGroup routeGroup) throws BusinessException {
        if (Objects.isNull(routeGroupMapper.selectByPrimaryKey(routeGroup.getId()))) {
            throw new BusinessException(String.format("group: {%s} not exists.", routeGroup));
        }
        routeGroupMapper.updateByPrimaryKey(routeGroup);
        routesMap.put(routeGroup.getId(), new ConcurrentHashMap<>());
    }

    @Override
    public RouteGroup getRouteGroup(String id) {
        return routeGroupMapper.selectByPrimaryKey(new RouteGroup(id));
    }

    @Override
    public List<RouteGroup> getRouteGroups() {
        return routeGroupMapper.selectAll();
    }

    @Override
    public List<Route> getRoutes(String groupId) {
        String path = StringUtil.generatePath(groupId);
        Example example = new Example(Route.class);
        example.createCriteria().andEqualTo("groupId", groupId);
        return routeMapper.selectByExample(example);
    }

    @Override
    public void deleteRoute(String groupId, String routeId) {
        Example example = new Example(Route.class);
        example.createCriteria()
                .andEqualTo("groupId", groupId)
                .andEqualTo("id", routeId);
        routeMapper.deleteByExample(example);
        routesMap.get(groupId).remove(routeId);
    }

    @Override
    public void addRoute(String groupId, Route route) throws Exception {
        if (StringUtil.isBlank(route.getServiceId()) && StringUtil.isBlank(route.getUrl())) {
            throw new Exception("service id和url不能同时为空");
        }
        if (StringUtil.isNotBlank(route.getServiceId()) && StringUtil.isNotBlank(route.getUrl())) {
            throw new Exception("service id和url不能同时设置");
        }
        Route query = new Route();
        query.setPath(route.getPath());
        if (!CollectionUtils.isEmpty(routeMapper.select(query))) {
            throw new BusinessException(String.format("route: {%s} already exists.", route));
        }
        routeMapper.insert(route);

        ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
        BeanUtils.copyProperties(route, zuulRoute);

        if (Objects.isNull(routesMap.get(groupId))) {
            routesMap.put(groupId, new ConcurrentHashMap<>());
        }
        routesMap.get(groupId).put(route.getId(), zuulRoute);
    }

    @Override
    public void updateRoute(String groupId, Route route) throws Exception {
        if (StringUtil.isBlank(route.getServiceId()) && StringUtil.isBlank(route.getUrl())) {
            throw new Exception("service id和url不能同时为空");
        }
        if (StringUtil.isNotBlank(route.getServiceId()) && StringUtil.isNotBlank(route.getUrl())) {
            throw new Exception("service id和url不能同时设置");
        }
        if (Objects.isNull(routeMapper.selectByPrimaryKey(route.getId()))) {
            throw new BusinessException(String.format("route: {%s} not exists.", route));
        }

        routeMapper.updateByPrimaryKey(route);
        routesMap.get(groupId).remove(route.getId());

        ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
        BeanUtils.copyProperties(route, zuulRoute);
        routesMap.get(groupId).put(route.getId(), zuulRoute);
    }

    @Override
    public void reload() {
        loadAllRoutes();
    }

    //TODO 修改这个方法，实现发布和下线
    public Map<String, Map<String, ZuulProperties.ZuulRoute>> getAllRoutes() {
        return routesMap;
    }

}