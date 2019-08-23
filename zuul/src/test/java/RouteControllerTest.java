import me.huzhiwei.zuul.ZuulApplication;
import me.huzhiwei.zuul.constant.Constant;
import me.huzhiwei.zuul.domain.RouteAddRO;
import me.huzhiwei.zuul.domain.ServiceAddRO;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-07 23:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZuulApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RouteControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void testCAddRoute() throws Exception {
		RouteAddRO zuulRouteRO = new RouteAddRO();
		zuulRouteRO.setName("user");
		zuulRouteRO.setPath("/user/**");
		zuulRouteRO.setStripPrefix(false);
		zuulRouteRO.setServiceId("user-service");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/gateway/routes").content(Constant.GSON.toJson(zuulRouteRO)).contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testDGetRoutes() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/gateway/routes"))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testFDeleteRoute() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/gateway/routes/"))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testCheckService() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/gateway/consul/services/zuul-service-192-168-38-213-10002"))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testAddService() throws Exception {
		ServiceAddRO serviceAddRO = new ServiceAddRO();
		serviceAddRO.setAddress("127.0.0.1");
		serviceAddRO.setPort(9000);
		serviceAddRO.setService("demo-service");
		serviceAddRO.setCheckUrl("/consul/check");
		serviceAddRO.setInterval("10s");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/gateway/consul/services").content(Constant.GSON.toJson(serviceAddRO)).contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

}
