package me.huzhiwei.zuul;

import me.huzhiwei.zuul.service.RefreshService;
import me.huzhiwei.zuul.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {
	@Autowired
	@Qualifier("zookeeperRouteServiceImpl")
	private RouteService routeService;

	@Autowired
	private RefreshService refreshService;

	@Autowired
	private ZuulHandlerMapping zuulHandlerMapping;

	@GetMapping("/routes")
	public Object getRoutes() {
		return routeService.getRoutes();
	}

	@DeleteMapping("/route/{id}")
	public Object delRoute(@PathVariable String id) {
		routeService.delRoute(id);
		refreshService.refreshRoute();
		return "success";
	}

	@PostMapping("/route")
	public Object addRoute(@RequestBody ZuulProperties.ZuulRoute zuulRoute) {
		routeService.addRoute(zuulRoute);
		refreshService.refreshRoute();
		return "success";
	}

	@GetMapping("/refreshRoute")
	public String refresh() {
		routeService.reload();
		refreshService.refreshRoute();
		return "refresh success";
	}

	@RequestMapping("/watchRoute")
	public Object watchNowRoute() {
		//可以用debug模式看里面具体是什么
		return zuulHandlerMapping.getHandlerMap();
	}
}
