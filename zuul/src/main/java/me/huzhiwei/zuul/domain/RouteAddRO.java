package me.huzhiwei.zuul.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-08 16:31
 */
@Data
public class RouteAddRO {

	private String id;

	@NotBlank
	private String name;

	@NotBlank
	private String path;

	private String serviceId;

	private String url;

	private Boolean stripPrefix = false;

	private Boolean retryable;

	@NotBlank
	private String groupId;
}
