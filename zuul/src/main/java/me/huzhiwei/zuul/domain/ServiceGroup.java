package me.huzhiwei.zuul.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-21 10:15
 */
@Data
@AllArgsConstructor
public class ServiceGroup {
    private String serviceName;
    private List<ServiceVO> serviceList;
}
