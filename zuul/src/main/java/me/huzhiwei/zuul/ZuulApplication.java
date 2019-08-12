package me.huzhiwei.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import tk.mybatis.spring.annotation.MapperScan;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties
@MapperScan(basePackages = "me.huzhiwei.zuul.mapper")
public class ZuulApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}

}
