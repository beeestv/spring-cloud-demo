import me.huzhiwei.zuul.ZuulApplication;
import me.huzhiwei.zuul.domain.RequestQuery;
import me.huzhiwei.zuul.mapper.RequestMapper;
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

	@Autowired
	private RequestMapper requestMapper;

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

}
