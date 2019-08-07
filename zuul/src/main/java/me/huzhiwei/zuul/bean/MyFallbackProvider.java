package me.huzhiwei.zuul.bean;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class MyFallbackProvider implements FallbackProvider {

	public String getRoute() {
		return "*";
	}

	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		return new ClientHttpResponse() {
			@Override
			public HttpStatus getStatusCode() {
				return HttpStatus.OK;
			}

			@Override
			public int getRawStatusCode() {
				return 200;
			}

			@Override
			public String getStatusText() {
				return "OK";
			}

			@Override
			public void close() {
			}

			@Override
			public InputStream getBody() {
				String response = "{\"message\":\"网关：请求服务异常\"}";
				return new ByteArrayInputStream(response.getBytes());
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				return httpHeaders;
			}
		};
	}
}
