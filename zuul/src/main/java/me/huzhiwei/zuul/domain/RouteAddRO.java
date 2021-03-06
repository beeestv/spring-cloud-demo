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

	@NotBlank
	private String name;

	@NotBlank
	private String path;

	@NotBlank
	private String serviceId;

	private Boolean stripPrefix = false;

	private Boolean online = false;
}
