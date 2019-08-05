package me.huzhiwei.zuul;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperCuratorClient implements InitializingBean {
	private CuratorFramework curatorClient;

	@Value("${apache.zookeeper.address:localhost:2181}")
	private String connectString;
	@Value("${apache.zookeeper.baseSleepTimeMs:1000}")
	private int baseSleepTimeMs;
	@Value("${apache.zookeeper.maxRetries:3}")
	private int maxRetries;
	@Value("${apache.zookeeper.sessionTimeoutMs:6000}")
	private int sessionTimeoutMs;
	@Value("${apache.zookeeper.connectionTimeoutMs:6000}")
	private int connectionTimeoutMs;

	@Override
	public void afterPropertiesSet() throws Exception {
		// custom policy
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
		// to build curatorClient
		curatorClient = CuratorFrameworkFactory.builder().connectString(connectString)
				.sessionTimeoutMs(sessionTimeoutMs).connectionTimeoutMs(connectionTimeoutMs)
				.retryPolicy(retryPolicy).build();
		curatorClient.start();
	}

	@Bean
	public CuratorFramework getCuratorClient() {
		return curatorClient;
	}
}