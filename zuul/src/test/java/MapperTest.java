import me.huzhiwei.zuul.ZuulApplication;
import me.huzhiwei.zuul.domain.RouteGroup;
import me.huzhiwei.zuul.mapper.RouteGroupMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

	@Test
	public void testAInsertRouteGroup() {
		RouteGroup routeGroup = new RouteGroup();
		routeGroup.setOnline(true);
		routeGroup.setName("user");
		routeGroupMapper.insert(routeGroup);
		routeGroupMapper.selectAllRouteGroups().forEach(System.out::println);
	}

}
