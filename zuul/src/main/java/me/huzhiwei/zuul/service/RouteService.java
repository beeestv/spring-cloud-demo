package me.huzhiwei.zuul.service;

import me.huzhiwei.zuul.domain.RouteGroup;
import me.huzhiwei.zuul.exception.ZkException;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.List;
import java.util.Map;

public interface RouteService {

	Map<String, Map<String, ZuulProperties.ZuulRoute>> getAllRoutes();

	List<ZuulProperties.ZuulRoute> getRoutes(String groupId) throws ZkException;

	void deleteRoute(String groupId, String routeId) throws ZkException;

	void addRoute(String groupId, ZuulProperties.ZuulRoute zuulRoute) throws ZkException;

	void reload() throws ZkException;

	void deleteRouteGroup(String id) throws ZkException;

	void addRouteGroup(RouteGroup routeGroup) throws ZkException;

	RouteGroup getRouteGroup(String id) throws ZkException;

	List<RouteGroup> getRouteGroups() throws ZkException;

	boolean checkExists(RouteGroup routeGroup) throws ZkException;

	boolean checkExists(String groupId, ZuulProperties.ZuulRoute zuulRoute) throws ZkException;
}
