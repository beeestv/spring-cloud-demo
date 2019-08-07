package me.huzhiwei.zuul.service;

import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.exception.ZkException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.DeleteBuilder;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static me.huzhiwei.zuul.constant.Constant.GSON;
import static me.huzhiwei.zuul.constant.Constant.ROOT_PATH;

@Service
@Slf4j
public class ZkService {

	@Autowired
	private CuratorFramework curatorClient;

	void deleteRoute(String path) throws ZkException {
		deleteRoute(path, false);
	}

	private void deleteRoute(String path, Boolean deletingChildren) throws ZkException {
		if (!checkExists(path)) return;

		String realPath = ROOT_PATH + path;
		try {
			DeleteBuilder delete = curatorClient.delete();
			if (deletingChildren) {
				delete.deletingChildrenIfNeeded().forPath(realPath);
			} else {
				delete.forPath(realPath);
			}
		} catch (Exception e) {
			String message = String.format("path: %s delete with children failed.", realPath);
			log.error(message, e);
			throw new ZkException(message, e);
		}
	}

	boolean checkExists(String path) throws ZkException {
		String realPath = ROOT_PATH + path;
		Stat stat;
		try {
			stat = curatorClient.checkExists().forPath(realPath);
		} catch (Exception e) {
			String message = String.format("path: %s checkExists failed.", realPath);
			log.error(message, e);
			throw new ZkException(message, e);
		}
		return stat != null;
	}

	void addNodeIfNotExist(String path) throws ZkException {
		if (!checkExists(path)) {
			String realPath = ROOT_PATH + path;
			try {
				curatorClient.create().creatingParentContainersIfNeeded().forPath(realPath);
			} catch (Exception e) {
				String message = String.format("path: %s creatingParentContainersIfNeeded failed.", realPath);
				log.error(message, e);
				throw new ZkException(message, e);
			}
		}
	}

	void setNodeValue(String path, Object value) throws ZkException {
		String realPath = ROOT_PATH + path;
		try {
			curatorClient.setData().forPath(realPath, GSON.toJson(value).getBytes());
		} catch (Exception e) {
			String message = String.format("path: %s setData failed.", realPath);
			log.error(message, e);
			throw new ZkException(message, e);
		}
	}

	<T> T getNodeValue(String path, Class<T> clazz) throws ZkException {
		String realPath = ROOT_PATH + path;
		byte[] data;
		try {
			data = curatorClient.getData().forPath(realPath);
		} catch (Exception e) {
			String message = String.format("path: %s getData failed.", realPath);
			log.error(message, e);
			throw new ZkException(message, e);
		}
		return GSON.fromJson(new String(data), clazz);
	}

	<T> List<T> getChildrenValue(String path, Class<T> clazz) throws ZkException {
		List<T> valueList = new ArrayList<>();
		List<String> children = getChildren(path);
		for (String node : children) {
			valueList.add(getNodeValue(path + "/" + node, clazz));
		}
		return valueList;
	}

	List<String> getChildren(String path) throws ZkException {
		String realPath = ROOT_PATH + path;
		List<String> children;
		try {
			children = curatorClient.getChildren().forPath(realPath);
		} catch (Exception e) {
			String message = String.format("path: %s getChildren failed.", realPath);
			log.error(message, e);
			throw new ZkException(message, e);
		}
		return children;
	}

}
