package me.huzhiwei.zuul;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

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
		routesMap.putAll(locateRoutesFromDB());
		return routesMap;
	}

	private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDB() {
		Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();
		List<ZuulRouteVO> results = new ArrayList<>();
		results.add(ZuulRouteVO.builder().id("user-service").path("/user/**").stripPrefix(false).serviceId("user-service").build());
		results.add(ZuulRouteVO.builder().id("asset-service").path("/asset/**").stripPrefix(false).serviceId("asset-service").build());

		for (ZuulRouteVO result : results) {
			if (Objects.isNull(result.getId())) {
				continue;
			}
			if (org.apache.commons.lang.StringUtils.isBlank(result.getPath()) && org.apache.commons.lang.StringUtils.isBlank(result.getUrl())) {
				continue;
			}
			ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
			try {
				org.springframework.beans.BeanUtils.copyProperties(result, zuulRoute);
			} catch (Exception e) {
				logger.error("=============load zuul route info from db with error==============", e);
			}
			routes.put(zuulRoute.getPath(), zuulRoute);
		}
		return routes;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ZuulRouteVO {

		/**
		 * The ID of the route (the same as its map key by default).
		 */
		private String id;

		/**
		 * The path (pattern) for the route, e.g. /foo/**.
		 */
		private String path;

		/**
		 * The service ID (if any) to map to this route. You can specify a physical URL or
		 * a service, but not both.
		 */
		private String serviceId;

		/**
		 * A full physical URL to map to the route. An alternative is to use a service ID
		 * and service discovery to find the physical address.
		 */
		private String url;

		/**
		 * Flag to determine whether the prefix for this route (the path, minus pattern
		 * patcher) should be stripped before forwarding.
		 */
		private boolean stripPrefix = true;

		/**
		 * Flag to indicate that this route should be retryable (if supported). Generally
		 * retry requires a service ID and ribbon.
		 */
		private Boolean retryable;

		private String apiName;

		private Boolean enabled;
	}
}
