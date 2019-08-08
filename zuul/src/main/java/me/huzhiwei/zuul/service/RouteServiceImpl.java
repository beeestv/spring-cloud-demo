package me.huzhiwei.zuul.service;

import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.domain.Route;
import me.huzhiwei.zuul.domain.RouteGroup;
import me.huzhiwei.zuul.exception.ZkException;
import me.huzhiwei.zuul.util.StringUtil;
import me.huzhiwei.zuul.util.UuidUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RouteServiceImpl implements RouteService {

	@Autowired
	private ZkService zkService;

	/**
	 * 目前生效的route table
	 * name -> ZuulRoute
	 */
	private static Map<String, Map<String, ZuulProperties.ZuulRoute>> routesMap;

	@PostConstruct
	public void init() {
		try {
			loadAllRoutes();
		} catch (ZkException e) {
			log.error("init all routes failed.", e);
		}
	}

	private void loadAllRoutes() throws ZkException {
		//清空routesMap
		routesMap = new ConcurrentHashMap<>();

		zkService.addNodeIfNotExist("");
		for (String id : zkService.getChildren("")) {
			routesMap.put(id, new ConcurrentHashMap<>());
			String path = StringUtil.generatePath(id);

			ZuulProperties.ZuulRoute zuulRoute;
			for (Route route : zkService.getChildrenValue(path, Route.class)) {
				zuulRoute = new ZuulProperties.ZuulRoute();
				BeanUtils.copyProperties(route, zuulRoute);
				zuulRoute.setId(route.getName());

				routesMap.get(id).put(route.getId(), zuulRoute);
			}
		}
	}

	@Override
	public void deleteRouteGroup(String id) throws ZkException {
		zkService.deleteRoute(StringUtil.generatePath(id));
		routesMap.remove(id);
	}

	@Override
	public void addRouteGroup(RouteGroup routeGroup) throws ZkException {
		routeGroup.setId(String.valueOf(UuidUtil.getId()));
		String path = StringUtil.generatePath(routeGroup.getId());
		if (zkService.checkExists(path)) {
			throw new ZkException(String.format("group: %s already exists.", path));
		}
		zkService.addNodeIfNotExist(path);
		zkService.setNodeValue(path, routeGroup);
		routesMap.put(routeGroup.getId(), new ConcurrentHashMap<>());
	}

	@Override
	public void updateRouteGroup(RouteGroup routeGroup) throws ZkException {
		String path = StringUtil.generatePath(routeGroup.getId());

		if (!zkService.checkExists(path)) {
			throw new ZkException(String.format("group: %s not exists.", path));
		}

		zkService.setNodeValue(path, routeGroup);
		routesMap.put(routeGroup.getId(), new ConcurrentHashMap<>());
	}

	@Override
	public RouteGroup getRouteGroup(String id) throws ZkException {
		String path = StringUtil.generatePath(id);
		return zkService.getNodeValue(path, RouteGroup.class);
	}

	@Override
	public List<RouteGroup> getRouteGroups() throws ZkException {
		return zkService.getChildrenValue("", RouteGroup.class);
	}

	@Override
	public List<Route> getRoutes(String groupId) throws ZkException {
		String path = StringUtil.generatePath(groupId);
		return zkService.getChildrenValue(path, Route.class);
	}

	@Override
	public void deleteRoute(String groupId, String routeId) throws ZkException {
		String path = StringUtil.generatePath(groupId, routeId);
		if (!zkService.checkExists(path)) return;

		Route oldRoute = zkService.getNodeValue(path, Route.class);
		routesMap.get(groupId).remove(oldRoute.getId());

		zkService.deleteRoute(path);
	}

	@Override
	public void addRoute(String groupId, Route route) throws Exception {
		if (StringUtil.isBlank(route.getServiceId()) && StringUtil.isBlank(route.getUrl())) {
			throw new Exception("service id和url不能同时为空");
		}
		if (StringUtil.isNotBlank(route.getServiceId()) && StringUtil.isNotBlank(route.getUrl())) {
			throw new Exception("service id和url不能同时设置");
		}
		route.setId(String.valueOf(UuidUtil.getId()));
		String path = StringUtil.generatePath(groupId, route.getId());
		if (zkService.checkExists(path)) {
			throw new ZkException(String.format("group: %s route: %s already exists.", groupId, route.getId()));
		}
		zkService.addNodeIfNotExist(path);
		zkService.setNodeValue(path, route);

		ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
		BeanUtils.copyProperties(route, zuulRoute);
		//Route的name是ZuulRoute的id
		zuulRoute.setId(route.getName());
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
		String path = StringUtil.generatePath(groupId, route.getId());
		if (!zkService.checkExists(path)) {
			throw new ZkException(String.format("group: %s route: %s not exists.", groupId, route.getId()));
		}

		Route oldRoute = zkService.getNodeValue(path, Route.class);
		routesMap.get(groupId).remove(oldRoute.getId());

		zkService.setNodeValue(path, route);

		ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
		BeanUtils.copyProperties(route, zuulRoute);
		//Route的name是ZuulRoute的id
		zuulRoute.setId(route.getName());
		routesMap.get(groupId).put(route.getId(), zuulRoute);
	}

	@Override
	public void reload() throws ZkException {
		loadAllRoutes();
	}

	@Override
	public boolean checkExists(String groupId, Route route) throws ZkException {
		return zkService.checkExists(StringUtil.generatePath(groupId, route.getId()));
	}

	@Override
	public boolean checkExists(RouteGroup routeGroup) throws ZkException {
		return zkService.checkExists(StringUtil.generatePath(routeGroup.getId()));
	}

	//TODO 修改这个方法，实现发布和下线
	public Map<String, Map<String, ZuulProperties.ZuulRoute>> getAllRoutes() {
		return routesMap;
	}

}