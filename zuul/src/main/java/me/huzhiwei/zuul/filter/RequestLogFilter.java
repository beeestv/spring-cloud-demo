package me.huzhiwei.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.domain.Request;
import me.huzhiwei.zuul.mapper.RequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class RequestLogFilter extends ZuulFilter {
	private static ExecutorService executorService = Executors.newFixedThreadPool(10);

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
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		Request request = new Request(ctx);
		executorService.execute(() -> requestMapper.insert(request));
		return null;
	}
}
