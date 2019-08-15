package me.huzhiwei.zuul.filter;

import com.netflix.util.Pair;
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

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-13 08:40
 */
@Component
@Slf4j
public class PostRequestLogFilter extends ZuulFilter {
    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Autowired
    private RequestMapper requestMapper;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 200000;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.containsKey(Constant.REQUEST_ID);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Long payload = Objects.isNull(ctx.getOriginContentLength()) ? 0L : ctx.getOriginContentLength();
        Long headers = 0L;
        for (Pair<String, String> responseHeader : (ArrayList<Pair<String, String>>)(ctx.get("zuulResponseHeaders"))) {
            headers += responseHeader.first().length();
            headers += responseHeader.second().length();
        }
        String requestId = (String) ctx.get(Constant.REQUEST_ID);
        Request update = new Request();
        update.setResponseSize(payload + headers);

        executorService.execute(() -> {
            Example example = new Example(Request.class);
            example.createCriteria().andEqualTo("requestId", requestId);
            requestMapper.updateByExampleSelective(update, example);
        });
        return null;
    }
}
