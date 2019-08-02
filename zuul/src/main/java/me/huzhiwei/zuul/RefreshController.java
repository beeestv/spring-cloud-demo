package me.huzhiwei.zuul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshController {
    @Autowired
    RefreshRouteService refreshRouteService;
 
    @Autowired
    ZuulHandlerMapping zuulHandlerMapping;
 
    @GetMapping("/refreshRoute")
    public String refresh() {
        refreshRouteService.refreshRoute();
        return "refresh success";
    }
 
    @RequestMapping("/watchRoute")
    public Object watchNowRoute() {
        //可以用debug模式看里面具体是什么
        return zuulHandlerMapping.getHandlerMap();
    }
}