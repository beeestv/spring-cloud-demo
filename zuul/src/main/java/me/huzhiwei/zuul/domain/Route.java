package me.huzhiwei.zuul.domain;

import lombok.Data;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-07 16:12
 */
@Data
public class Route {

	private String id;

	private String name;

	private String path;

	private String serviceId;

	private String url;

	private boolean stripPrefix = false;

	private Boolean retryable;
}
