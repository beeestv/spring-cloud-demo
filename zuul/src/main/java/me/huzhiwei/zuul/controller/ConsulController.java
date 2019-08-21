package me.huzhiwei.zuul.controller;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.Service;
import me.huzhiwei.zuul.domain.Result;
import me.huzhiwei.zuul.domain.ServiceAddRO;
import me.huzhiwei.zuul.domain.ServiceGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-20 17:15
 */
@RestController
@RequestMapping("/zuul/consul")
public class ConsulController {

    @Autowired
    private ConsulClient consulClient;

    @GetMapping("/services")
    public Result getAllService() {
        Response<Map<String, Service>> agentServices = consulClient.getAgentServices();
        List<ServiceGroup> serviceGroups = agentServices.getValue()
                .values().stream()
                .collect(Collectors.groupingBy(Service::getService))
                .values().stream()
                .map(services -> new ServiceGroup(services.get(0).getService(), services))
                .collect(Collectors.toList());
        return Result.success(serviceGroups);
    }

    @PostMapping("/services")
    public Result addService(@RequestBody ServiceAddRO serviceAddRO) {
        NewService newService = new NewService();
        newService.setId(serviceAddRO.getName() + "-" + serviceAddRO.getAddress().replace(".", "-") + "-" + serviceAddRO.getPort());
        newService.setName(serviceAddRO.getName());
        newService.setAddress(serviceAddRO.getAddress());
        newService.setPort(serviceAddRO.getPort());

        NewService.Check check = new NewService.Check();
        check.setHttp(String.format("http://%s:%d%s", serviceAddRO.getAddress(), serviceAddRO.getPort(), serviceAddRO.getCheckUrl()));
        check.setInterval(serviceAddRO.getInterval());
        newService.setCheck(check);
        consulClient.agentServiceRegister(newService);
        return Result.success();
    }

    @DeleteMapping("/services/{serviceId}")
    public Result deleteService(@PathVariable String serviceId) {
        consulClient.agentServiceDeregister(serviceId);
        return Result.success();
    }

}
