package me.huzhiwei.zuul.domain;

import com.netflix.zuul.context.RequestContext;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Optional;

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
    private String routeId;
    private String host;
    private String method;
    private String url;
    private String uri;
    private String routeHost;
    private String serviceId;
    private Long requestTime;

    public Request(RequestContext requestContext) {
        HttpServletRequest request = requestContext.getRequest();
        this.routeId = (String) requestContext.get("proxy");
        this.host = request.getHeader("host");
        this.method = request.getMethod();
        this.url = request.getRequestURL().toString();
        this.uri = (String) requestContext.get("requestURI");
        Optional.ofNullable(requestContext.get("routeHost"))
                .ifPresent(routeHost -> this.routeHost = routeHost.toString());
        this.serviceId = (String) requestContext.get("serviceId");
        this.requestTime = System.currentTimeMillis();
    }
}
