package me.huzhiwei.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.constant.Constant;
import me.huzhiwei.zuul.domain.Request;
import me.huzhiwei.zuul.mapper.RequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class RouteRequestLogFilter extends ZuulFilter {
	private static ExecutorService executorService = Executors.newFixedThreadPool(20);

	@Autowired
	private RequestMapper requestMapper;

	@Override
	public String filterType() {
		return FilterConstants.ROUTE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		return ctx.containsKey(Constant.REQUEST_ID);
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String requestId = (String) ctx.get(Constant.REQUEST_ID);
		ctx.set(Constant.REQUEST_ID, requestId);

		Request request = new Request();
		request.setRouteId((String) ctx.get("proxy"));
		Optional.ofNullable(ctx.get("routeHost"))
				.ifPresent(routeHost -> request.setRouteHost(routeHost.toString()));
		request.setUri((String) ctx.get("requestURI"));
		request.setServiceId((String) ctx.get("serviceId"));

		executorService.execute(() -> {
			Example example = new Example(Request.class);
			example.createCriteria().andEqualTo("requestId", requestId);
			requestMapper.updateByExampleSelective(request, example);
		});
		return null;
	}
}
