package me.huzhiwei.zuul.service;

import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.domain.RouteGroup;
import me.huzhiwei.zuul.exception.ZkException;
import me.huzhiwei.zuul.util.StringUtil;
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
	 * id -> ZuulRoute
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
			for (ZuulProperties.ZuulRoute zuulRoute : zkService.getChildrenValue(path, ZuulProperties.ZuulRoute.class)) {
				routesMap.get(id).put(zuulRoute.getId(), zuulRoute);
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
		String path = StringUtil.generatePath(routeGroup.getId());
		if (zkService.checkExists(path)) {
			throw new ZkException(String.format("group: %s already exists.", path));
		}
		zkService.addNodeIfNotExist(path);
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
	public List<ZuulProperties.ZuulRoute> getRoutes(String groupId) throws ZkException {
		String path = StringUtil.generatePath(groupId);
		return zkService.getChildrenValue(path, ZuulProperties.ZuulRoute.class);
	}

	@Override
	public void deleteRoute(String groupId, String routeId) throws ZkException {
		String path = StringUtil.generatePath(groupId, routeId);
		if (!zkService.checkExists(path)) return;
		zkService.deleteRoute(path);
		routesMap.get(groupId).remove(routeId);
	}

	@Override
	public void addRoute(String groupId, ZuulProperties.ZuulRoute zuulRoute) throws ZkException {
		String path = StringUtil.generatePath(groupId, zuulRoute.getId());
		if (zkService.checkExists(path)) {
			throw new ZkException(String.format("group: %s route: %s already exists.", groupId, zuulRoute.getId()));
		}
		zkService.addNodeIfNotExist(path);
		zkService.setNodeValue(path, zuulRoute);
		routesMap.get(groupId).put(zuulRoute.getId(), zuulRoute);
	}

	@Override
	public void reload() throws ZkException {
		loadAllRoutes();
	}

	//TODO 修改这个方法，实现发布和下线
	public Map<String, Map<String, ZuulProperties.ZuulRoute>> getAllRoutes() {
		return routesMap;
	}

}