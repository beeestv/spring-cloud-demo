import me.huzhiwei.zuul.ZuulApplication;
import me.huzhiwei.zuul.constant.Constant;
import me.huzhiwei.zuul.domain.RouteAddRO;
import me.huzhiwei.zuul.domain.RouteGroup;
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
	public void testAAddRouteGroup() throws Exception {
		RouteGroup routeGroup = new RouteGroup();
		routeGroup.setName("business");
		routeGroup.setOnline(true);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/route/groups").content(Constant.GSON.toJson(routeGroup)).contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testCAddRoute() throws Exception {
		RouteAddRO zuulRouteRO = new RouteAddRO();
		zuulRouteRO.setName("user");
		zuulRouteRO.setPath("/user/**");
		zuulRouteRO.setStripPrefix(false);
		zuulRouteRO.setServiceId("user-service");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/route/groups/business/routes").content(Constant.GSON.toJson(zuulRouteRO)).contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testCGetRouteGroups() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/route/groups"))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testDGetRoutes() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/route/groups/business/routes"))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testEDeleteRouteGroup() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/route/groups/business"))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testFDeleteRoute() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/route/groups/business/routes/user"))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testGDeleteRouteGroup2() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/route/groups/business"))
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}
}
