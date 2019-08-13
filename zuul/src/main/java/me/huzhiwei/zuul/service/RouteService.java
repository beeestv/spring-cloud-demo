package me.huzhiwei.zuul.service;

import me.huzhiwei.zuul.domain.Route;
import me.huzhiwei.zuul.domain.RouteGroup;
import me.huzhiwei.zuul.exception.BusinessException;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.List;
import java.util.Map;

public interface RouteService {

	Map<String, Map<String, ZuulProperties.ZuulRoute>> getAllRoutes();

	List<Route> getRoutes(String groupId);

	void deleteRoute(String groupId, String routeId);

	void addRoute(String groupId, Route zuulRoute) throws Exception;

	void reload();

	void deleteRouteGroup(String id);

	void addRouteGroup(RouteGroup routeGroup) throws BusinessException;

	RouteGroup getRouteGroup(String id);

	List<RouteGroup> getRouteGroups();

	void updateRoute(String groupId, Route route) throws Exception;

	void updateRouteGroup(RouteGroup routeGroup) throws BusinessException;
}
