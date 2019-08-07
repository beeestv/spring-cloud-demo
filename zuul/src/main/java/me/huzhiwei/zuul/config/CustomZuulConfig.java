package me.huzhiwei.zuul.config;

import me.huzhiwei.zuul.bean.CustomRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomZuulConfig {
    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;

    @Bean
    public CustomRouteLocator routeLocator() {
        return new CustomRouteLocator(this.server.getServlet().getContextPath(),
                this.zuulProperties);
    }
}