package me.huzhiwei.zuul.domain;

import lombok.Data;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-21 16:31
 */
@Data
public class Client {
    private Long id;
    private String clientName;
    private String clientId;
    private String clientSecret;
    private String resourceIds;
    private String scope;
}
