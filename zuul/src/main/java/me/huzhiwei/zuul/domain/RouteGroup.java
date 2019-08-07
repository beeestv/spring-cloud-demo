package me.huzhiwei.zuul.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RouteGroup {

	@NotNull
	private String id;

	@NotNull
	private Boolean online;

}
