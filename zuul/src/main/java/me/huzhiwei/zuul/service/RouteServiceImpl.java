package me.huzhiwei.zuul.service;

import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.domain.Route;
import me.huzhiwei.zuul.domain.RouteQuery;
import me.huzhiwei.zuul.exception.BusinessException;
import me.huzhiwei.zuul.mapper.RouteMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@Transactional
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteMapper routeMapper;

    @Override
    public List<Route> getRoutes(RouteQuery query) {
        Example example = new Example(Route.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(query.getName())) {
            criteria.andLike("name", query.getName() + "%");
        }
        if (StringUtils.isNotBlank(query.getPath())) {
            criteria.andLike("path", query.getPath() + "%");
        }
        if (StringUtils.isNotBlank(query.getServiceId())) {
            criteria.andLike("serviceId", query.getServiceId() + "%");
        }
        return routeMapper.selectByExample(example);
    }

    @Override
    public void deleteRoute(String routeId) {
        Example example = new Example(Route.class);
        example.createCriteria()
                .andEqualTo("id", routeId);
        routeMapper.deleteByExample(example);
    }

    @Override
    public void addRoute(Route route) throws Exception {
        Route query = new Route();
        query.setPath(route.getPath());
        if (!CollectionUtils.isEmpty(routeMapper.select(query))) {
            throw new BusinessException(String.format("route: {%s} already exists.", route));
        }
        routeMapper.insert(route);

        ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
        BeanUtils.copyProperties(route, zuulRoute);
    }

    @Override
    public void updateRoute(Route route) throws Exception {
        if (Objects.isNull(routeMapper.selectByPrimaryKey(route.getId()))) {
            throw new BusinessException(String.format("route: {%s} not exists.", route));
        }

        routeMapper.updateByPrimaryKey(route);

        ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
        BeanUtils.copyProperties(route, zuulRoute);
    }

    @Override
    public Route getRoute(String routeId) {
        return routeMapper.selectByPrimaryKey(routeId);
    }

    public Map<String, ZuulProperties.ZuulRoute> getAllRoutes() {
        Example example = new Example(Route.class);
        example.createCriteria().andEqualTo("online", true);
        List<Route> allRoutes = routeMapper.selectByExample(example);

        Map<String, ZuulProperties.ZuulRoute> routesMap = new ConcurrentHashMap<>();
        ZuulProperties.ZuulRoute zuulRoute;
        for (Route route : allRoutes) {
            zuulRoute = new ZuulProperties.ZuulRoute();
            BeanUtils.copyProperties(route, zuulRoute);

            routesMap.put(route.getId(), zuulRoute);
        }
        return routesMap;
    }

}