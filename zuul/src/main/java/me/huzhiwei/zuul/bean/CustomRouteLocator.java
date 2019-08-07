package me.huzhiwei.zuul.bean;

import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

	@Autowired
	private RouteService routeService;

	private ZuulProperties properties;

	public CustomRouteLocator(String servletPath, ZuulProperties properties) {
		super(servletPath, properties);
		this.properties = properties;
		log.info("servletPath:{}", servletPath);
	}

	@Override
	public void refresh() {
		doRefresh();
	}

	@Override
	protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
		Map<String, ZuulProperties.ZuulRoute> routesMap = getAllRoutesMap();
		//优化一下配置
		LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
		for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
			String path = entry.getKey();
			// Prepend with slash if not already present.
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (StringUtils.hasText(this.properties.getPrefix())) {
				path = this.properties.getPrefix() + path;
				if (!path.startsWith("/")) {
					path = "/" + path;
				}
			}
			values.put(path, entry.getValue());
		}
		return values;
	}

	/**
	 * 获取Zuul Route
	 * 可以自定义各种数据源，如：application.properties
	 */
	private Map<String, ZuulProperties.ZuulRoute> getAllRoutesMap() {
		LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
		//从application.properties中加载路由信息
		routesMap.putAll(super.locateRoutes());
		//从db中加载路由信息
		routesMap.putAll(locateRoutesFromService());
		return routesMap;
	}

	private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromService() {
		List<ZuulProperties.ZuulRoute> values = new ArrayList<>();
		for (Map<String, ZuulProperties.ZuulRoute> value : routeService.getAllRoutes().values()) {
			values.addAll(value.values());
		}
		return values.stream().collect(Collectors.toMap(ZuulProperties.ZuulRoute::getPath, v -> v));
	}

}
