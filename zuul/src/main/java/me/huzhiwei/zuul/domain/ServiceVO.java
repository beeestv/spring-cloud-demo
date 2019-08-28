package me.huzhiwei.zuul.domain;

import com.ecwid.consul.v1.agent.model.Check;
import com.ecwid.consul.v1.agent.model.Service;
import lombok.Data;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-28 10:58
 */
@Data
public class ServiceVO extends Service {
    private String status;
    private String output;

    public ServiceVO(Service service, Check check) {
        if (service != null) {
            this.setId(service.getId());
            this.setService(service.getService());
            this.setAddress(service.getAddress());
            this.setPort(service.getPort());
        }
        if (check != null) {
            this.status = check.getStatus().name();
            this.output = check.getOutput();
        }
    }
}
