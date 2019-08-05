package me.huzhiwei.zuul.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service("zookeeperRouteServiceImpl")
@Slf4j
public class ZookeeperRouteServiceImpl implements RouteService {
	private static final String ROOT_PATH = "/routes";
	private static final Gson GSON = new Gson();

	@Autowired
	private CuratorFramework curatorClient;

	/**
	 * id -> ZuulRoute
	 */
	private static Map<String, ZuulProperties.ZuulRoute> routesMap;

	@PostConstruct
	public void init() {
		loadAllRoutes();
	}

	private void loadAllRoutes() {
		//清空routesMap
		routesMap = new ConcurrentHashMap<>();

		try {
			addNodeIfNotExist(ROOT_PATH);
		} catch (Exception e) {
			log.error(String.format("path: %s create failed.", ROOT_PATH), e);
		}
		try {
			curatorClient.getChildren().forPath(ROOT_PATH).forEach(id -> {
				String path = ROOT_PATH + "/" + id;
				try {
					ZuulProperties.ZuulRoute zuulRoute = getValue(path);
					routesMap.put(zuulRoute.getId(), zuulRoute);
				} catch (Exception e) {
					log.error(String.format("path: %s get children failed.", path), e);
				}
			});
		} catch (Exception e) {
			log.error(String.format("path: %s get children failed.", ROOT_PATH), e);
		}
	}

	public Map<String, ZuulProperties.ZuulRoute> getRoutes() {
		return routesMap;
	}

	public void addRoute(ZuulProperties.ZuulRoute zuulRoute) {
		if (Objects.isNull(zuulRoute.getId())) {
			return;
		}
		if (org.apache.commons.lang.StringUtils.isBlank(zuulRoute.getPath()) && org.apache.commons.lang.StringUtils.isBlank(zuulRoute.getUrl())) {
			return;
		}

		String path = ROOT_PATH + "/" + zuulRoute.getId();
		try {
			addNodeIfNotExist(path);
			updateValue(zuulRoute);
			routesMap.put(zuulRoute.getId(), zuulRoute);
		} catch (Exception e) {
			log.error(String.format("path: %s update value failed.", path), e);
		}
	}

	@Override
	public void reload() {
		loadAllRoutes();
	}

	public void delRoute(String id) {
		String path = ROOT_PATH + "/" + id;
		try {
			curatorClient.delete().forPath(path);
			routesMap.remove(id);
		} catch (Exception e) {
			log.error(String.format("path: %s delete failed.", path), e);
		}
	}


	private void addNodeIfNotExist(String path) throws Exception {
		Stat stat = curatorClient.checkExists().forPath(path);
		if (Objects.isNull(stat)) {
			curatorClient.create().creatingParentContainersIfNeeded().forPath(path);
		}
	}

	private void updateValue(ZuulProperties.ZuulRoute zuulRoute) throws Exception {
		curatorClient.setData().forPath(ROOT_PATH + "/" + zuulRoute.getId(), GSON.toJson(zuulRoute).getBytes());
	}

	private ZuulProperties.ZuulRoute getValue(String path) throws Exception {
		byte[] data = curatorClient.getData().forPath(path);
		return new Gson().fromJson(new String(data), ZuulProperties.ZuulRoute.class);
	}
}