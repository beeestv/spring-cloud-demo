import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Check;
import me.huzhiwei.zuul.ZuulApplication;
import me.huzhiwei.zuul.constant.Constant;
import me.huzhiwei.zuul.domain.Client;
import me.huzhiwei.zuul.domain.RequestQuery;
import me.huzhiwei.zuul.domain.Route;
import me.huzhiwei.zuul.mapper.ClientMapper;
import me.huzhiwei.zuul.mapper.RequestMapper;
import me.huzhiwei.zuul.mapper.RouteMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

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
        client.setClientId("tester2");
        client.setClientSecret("123456");
        client.setResourceIdsString(Constant.RESOURECE_ID);
        client.setScopeString("read");
        clientMapper.insert(client);
    }

    @Test
    public void testSelectClient() {
        List<Client> clients = clientMapper.selectListByClientId("te" + "%");
        System.out.println(clients);
    }

    @Autowired
    private ConsulClient consulClient;

    @Test
    public void testConsulClient() {
    }

    @Test
    public void testConsulClinetChecks() {
        Response<Map<String, Check>> agentChecks = consulClient.getAgentChecks();
        agentChecks.getValue().values().forEach(System.out::println);
    }

    @Autowired
    private RouteMapper routeMapper;

    @Test
    public void testGetRoutes() {
        Example example = new Example(Route.class);
        example.createCriteria().andEqualTo("online", true);
        List<Route> allRoutes = routeMapper.selectByExample(example);
        System.out.println(allRoutes);
    }
}
