package me.huzhiwei.zuul.controller;

import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.constant.Constant;
import me.huzhiwei.zuul.domain.Result;
import me.huzhiwei.zuul.domain.RouteGroup;
import me.huzhiwei.zuul.domain.ZuulRouteRO;
import me.huzhiwei.zuul.exception.ZkException;
import me.huzhiwei.zuul.service.RefreshService;
import me.huzhiwei.zuul.service.RouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/route")
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


	@GetMapping("/groups/{groupId}/routes")
	public Result getRoutes(@PathVariable String groupId) throws ZkException {
		return Result.success(routeService.getRoutes(groupId));
	}

	@DeleteMapping("/groups/{groupId}/routes/{routeId}")
	public Result delRoute(@PathVariable String groupId, @PathVariable String routeId) throws ZkException {
		routeService.deleteRoute(groupId, routeId);
		refreshService.refreshRoute();
		return Result.success();
	}

	@PostMapping("/groups/{groupId}/routes")
	public Result addRoute(@PathVariable String groupId, @Valid @RequestBody ZuulRouteRO zuulRouteRO) throws ZkException {
		ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
		BeanUtils.copyProperties(zuulRouteRO, zuulRoute);
		if (routeService.checkExists(groupId, zuulRoute)) {
			return Result.fail(Constant.ResultCode.INVALID_PARAMETER, String.format("group: %s, route: %s 已存在", groupId, zuulRoute.getId()));
		}
		routeService.addRoute(groupId, zuulRoute);
		refreshService.refreshRoute();
		return Result.success();
	}

	@GetMapping("/refreshRoute")
	public Result refresh() throws ZkException {
		routeService.reload();
		refreshService.refreshRoute();
		return Result.success();
	}

	@GetMapping("/groups")
	public Result getRouteGroups() throws ZkException {
		return Result.success(routeService.getRouteGroups());
	}

	@PostMapping("/groups")
	public Result addRouteGroup(@Valid @RequestBody RouteGroup routeGroup) throws ZkException {
		if (routeService.checkExists(routeGroup)) {
			return Result.fail(Constant.ResultCode.INVALID_PARAMETER, String.format("route group: %s 已存在", routeGroup.getId()));
		}
		routeService.addRouteGroup(routeGroup);
		return Result.success();
	}

	@DeleteMapping("/groups/{id}")
	public Result deleteRouteGroup(@PathVariable String id) throws ZkException {
		RouteGroup routeGroup = routeService.getRouteGroup(id);
		if (routeGroup == null) {
			return Result.fail(Constant.ResultCode.INVALID_PARAMETER, String.format("route group: %s 不存在", id));
		}
		routeService.deleteRouteGroup(id);
		refreshService.refreshRoute();
		return Result.success();
	}
}
