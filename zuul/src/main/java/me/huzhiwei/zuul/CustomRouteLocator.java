package me.huzhiwei.zuul;

import me.huzhiwei.zuul.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

	@Autowired
	private RouteService routeService;

	public final static Logger logger = LoggerFactory.getLogger(CustomRouteLocator.class);

	private ZuulProperties properties;

	public CustomRouteLocator(String servletPath, ZuulProperties properties) {
		super(servletPath, properties);
		this.properties = properties;
		logger.info("servletPath:{}", servletPath);
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
	 * @return
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
		Map<String, ZuulProperties.ZuulRoute> map = routeService.getRoutes().values().stream().collect(Collectors.toMap(ZuulProperties.ZuulRoute::getPath, v -> v));
		return map;
	}

}
