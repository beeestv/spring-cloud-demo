package me.huzhiwei.zuul.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-08 16:55
 */
@Data
public class RouteGroupAddRO {

	private String id;

	@NotBlank
	private String name;

	@NotNull
	private Boolean online;

	private String description;
}
