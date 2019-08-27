package me.huzhiwei.zuul.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.domain.Result;
import me.huzhiwei.zuul.domain.Route;
import me.huzhiwei.zuul.domain.RouteAddRO;
import me.huzhiwei.zuul.domain.RouteQuery;
import me.huzhiwei.zuul.service.RefreshService;
import me.huzhiwei.zuul.service.RouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/gateway/routes")
@Slf4j
public class RouteController {

	@Autowired
	private RouteService routeService;

	@Autowired
	private RefreshService refreshService;

	@Autowired
	private ZuulHandlerMapping zuulHandlerMapping;

	/**
	 * zuul实际加载的路由表
	 */
	@RequestMapping("/watchRoute")
	public Result watchNowRoute() {
		return Result.success(zuulHandlerMapping.getHandlerMap());
	}


	@GetMapping("/")
	public Result getRoute(RouteQuery query) {
		PageInfo<Route> pageInfo = PageHelper.startPage(query).doSelectPageInfo(() -> routeService.getRoutes(query));
		return Result.success(pageInfo);
	}

	@GetMapping("/{routeId}")
	public Result getRoute(@PathVariable String routeId) {
		Route route = routeService.getRoute(routeId);
		return Result.success(route);
	}

	@DeleteMapping("/{routeId}")
	public Result delRoute(@PathVariable String routeId) {
		routeService.deleteRoute(routeId);
		refreshService.refreshRoute();
		return Result.success();
	}

	@PostMapping("/")
	public Result addRoute(@Valid @RequestBody RouteAddRO routeAddRO) throws Exception {
		Route route = new Route();
		BeanUtils.copyProperties(routeAddRO, route);
		routeService.addRoute(route);
		refreshService.refreshRoute();
		return Result.success();
	}

	@PutMapping("/{routeId}")
	public Result updateRoute(@PathVariable String routeId, @Valid @RequestBody RouteAddRO routeAddRO) throws Exception {
		Route route = new Route();
		BeanUtils.copyProperties(routeAddRO, route);
		route.setId(routeId);
		routeService.updateRoute(route);
		refreshService.refreshRoute();
		return Result.success();
	}

	@GetMapping("/refreshRoute")
	public Result refresh() {
		refreshService.refreshRoute();
		return Result.success();
	}
}
