import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Service;
import me.huzhiwei.zuul.ZuulApplication;
import me.huzhiwei.zuul.config.OAuth2Config;
import me.huzhiwei.zuul.domain.Client;
import me.huzhiwei.zuul.domain.RequestQuery;
import me.huzhiwei.zuul.domain.ServiceGroup;
import me.huzhiwei.zuul.mapper.ClientMapper;
import me.huzhiwei.zuul.mapper.RequestMapper;
import me.huzhiwei.zuul.mapper.RouteGroupMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-08 19:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZuulApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapperTest {

    @Autowired
    private RouteGroupMapper routeGroupMapper;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Test
    public void testAInsertRouteGroup2() {
        RequestQuery query = new RequestQuery();
        System.out.println(requestMapper.overview(query));

        query.setGroupId("4");
        System.out.println(requestMapper.overview(query));

        query.setGroupId("2");
        System.out.println(requestMapper.overview(query));

        query.setRouteId("9");
        System.out.println(requestMapper.overview(query));

        query.setBeginTime(1L);
        System.out.println(requestMapper.overview(query));
    }

    @Test
    public void testInsertClient() {
        Client client = new Client();
        client.setClientId("tester");
        client.setClientSecret("123456");
        client.setResourceIds(OAuth2Config.RESOURECE_ID);
        client.setScope("read");
        client.setClientName(client.getClientId());
        clientMapper.insert(client);
    }

    @Autowired
    private ConsulClient consulClient;

    @Test
    public void testConsulClient() {
        Response<Map<String, Service>> agentServices = consulClient.getAgentServices();
        List<ServiceGroup> serviceGroups = agentServices.getValue()
                .values().stream()
                .collect(Collectors.groupingBy(Service::getService))
                .values().stream()
                .map(services -> new ServiceGroup(services.get(0).getService(), services))
                .collect(Collectors.toList());
        System.out.println(serviceGroups);
    }

}
