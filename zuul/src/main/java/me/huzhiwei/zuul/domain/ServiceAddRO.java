package me.huzhiwei.zuul.domain;

import lombok.Data;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-21 10:39
 */
@Data
public class ServiceAddRO {
    private String service;
    private String address;
    private Integer port;
    private Boolean enableTagOverride = false;

    /**
     * Check
     */
    private String interval;
    private String checkUrl;
    private String deregisterCriticalServiceAfter = "90m";
}
