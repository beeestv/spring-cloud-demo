package me.huzhiwei.zuul.service;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.Map;

public interface RouteService {
	Map<String, ZuulProperties.ZuulRoute> getRoutes();

	void delRoute(String id);

	void addRoute(ZuulProperties.ZuulRoute zuulRouteVO);

	void reload();
}
