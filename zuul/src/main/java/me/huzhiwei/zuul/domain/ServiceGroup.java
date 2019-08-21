package me.huzhiwei.zuul.domain;

import com.ecwid.consul.v1.agent.model.Service;
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
    private List<Service> serviceList;
}
