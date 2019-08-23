package me.huzhiwei.zuul.domain;

import lombok.Data;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-23 10:06
 */
@Data
public class RouteQuery extends Page {
    private String name;
    private String path;
    private String serviceId;
}
