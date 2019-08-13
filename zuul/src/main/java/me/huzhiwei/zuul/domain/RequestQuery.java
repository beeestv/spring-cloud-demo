package me.huzhiwei.zuul.domain;

import lombok.Data;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-12 15:46
 */
@Data
public class RequestQuery extends Query {
    private String groupId;
    private String routeId;
    private Long beginTime;
    private Long endTime = System.currentTimeMillis();
}
