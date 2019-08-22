package me.huzhiwei.zuul.controller;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.Service;
import com.ecwid.consul.v1.health.model.Check;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.huzhiwei.zuul.constant.Constant;
import me.huzhiwei.zuul.domain.Result;
import me.huzhiwei.zuul.domain.ServiceAddRO;
import me.huzhiwei.zuul.domain.ServiceGroup;
import me.huzhiwei.zuul.domain.ServiceWithCheck;
import me.huzhiwei.zuul.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-20 17:15
 */
@RestController
@RequestMapping("/gateway/consul")
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

    @GetMapping("/services/{serviceId}")
    public Result getServiceById(@PathVariable String serviceId) throws BusinessException {
        Response<Map<String, Service>> agentServices = consulClient.getAgentServices();

        Service service = null;
        for (Service s : agentServices.getValue().values()) {
            if (s.getId().equals(serviceId)) {
                service = s;
                break;
            }
        }

        if (service == null) {
            throw new BusinessException("service id not found.");
        }
        Map<String, Object> resultMap = new HashMap<>();
        Gson gson = Constant.GSON;
        resultMap.putAll(gson.fromJson(gson.toJson(service), new TypeToken<Map<String, Object>>(){}.getType()));

        Response<List<Check>> healthChecksForService = consulClient.getHealthChecksForService(service.getService(), null);
        for (Check check : healthChecksForService.getValue()) {
            if (check.getServiceId().equals(serviceId)) {
                resultMap.putAll(gson.fromJson(gson.toJson(check), new TypeToken<Map<String, Object>>(){}.getType()));
            }
        }

        ServiceWithCheck serviceWithCheck = gson.fromJson(gson.toJson(resultMap), ServiceWithCheck.class);
        return Result.success(serviceWithCheck);
    }

    @PostMapping("/services")
    public Result addService(@RequestBody ServiceAddRO serviceAddRO) {
        NewService newService = new NewService();
        newService.setId(serviceAddRO.getService() + "-" + serviceAddRO.getAddress().replace(".", "-") + "-" + serviceAddRO.getPort());
        newService.setName(serviceAddRO.getService());
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
