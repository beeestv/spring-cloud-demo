package me.huzhiwei.zuul.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-07 16:12
 */
@Data
public class ZuulRouteRO {

	@NotBlank
	private String id;

	@NotBlank
	private String path;

	private String serviceId;

	private String url;

	private boolean stripPrefix = false;

	private Boolean retryable;
}
