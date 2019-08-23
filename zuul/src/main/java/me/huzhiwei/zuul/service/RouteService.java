package me.huzhiwei.zuul.service;

import me.huzhiwei.zuul.domain.Route;
import me.huzhiwei.zuul.domain.RouteQuery;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.List;
import java.util.Map;

public interface RouteService {

	Map<String, ZuulProperties.ZuulRoute> getAllRoutes();

	List<Route> getRoutes(RouteQuery query);

	void deleteRoute(String routeId);

	void addRoute(Route zuulRoute) throws Exception;

	void updateRoute(Route route) throws Exception;

	Route getRoute(String routeId);
}
