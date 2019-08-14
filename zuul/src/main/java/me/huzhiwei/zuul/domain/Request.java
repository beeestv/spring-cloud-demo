package me.huzhiwei.zuul.domain;

import com.netflix.zuul.context.RequestContext;
import lombok.Data;
import me.huzhiwei.zuul.util.UuidUtil;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-12 11:34
 */
@Data
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @KeySql(useGeneratedKeys = true)
    private String id;
    private String requestId;
    private String routeId;
    private String clientIp;
    private String method;
    private String url;
    private String uri;
    private String routeHost;
    private String serviceId;
    private Long responseSize;
    private Long requestTime;

    public Request() {
    }

    public Request(RequestContext requestContext) {
        this.requestId = String.valueOf(UuidUtil.getId());
        initFromRequestContext(requestContext);
    }

    public Request(String requestId, RequestContext requestContext) {
        this.requestId = requestId;
        initFromRequestContext(requestContext);
    }

    private void initFromRequestContext(RequestContext requestContext) {
        HttpServletRequest request = requestContext.getRequest();
        this.method = request.getMethod();
        this.url = request.getRequestURL().toString();
        this.requestTime = System.currentTimeMillis();
        this.clientIp = request.getHeader("X-FORWARDED-FOR");
        if (this.clientIp == null) {
            this.clientIp = request.getRemoteAddr();
        } else {
            this.clientIp = this.clientIp.contains(",") ? this.clientIp.split(",")[0] : this.clientIp;
        }
    }
}
