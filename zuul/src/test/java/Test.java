import com.google.gson.Gson;
import me.huzhiwei.zuul.ZuulApplication;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZuulApplication.class)
public class Test {
	@Autowired
	private CuratorFramework curatorClient;

	private static String ROOT_PATH = "/routes";

	public void addChildren(String childrenPath) throws Exception {
		String path = ROOT_PATH + childrenPath;
		Stat stat = curatorClient.checkExists().forPath(path);
		if (stat != null) {
			throw new RuntimeException("ROOT_PATH = " + path + " has bean exist.");
		}
		curatorClient.create().creatingParentContainersIfNeeded().forPath(path);
	}

	public void setValue(ZuulProperties.ZuulRoute zuulRoute) throws Exception {
		curatorClient.setData().forPath(ROOT_PATH + "/" + zuulRoute.getId(), new Gson().toJson(zuulRoute).getBytes());
	}

	private void getValue(String path) throws Exception {
		Stat stat = new Stat();
		byte[] data = curatorClient.getData().storingStatIn(stat).forPath(ROOT_PATH + path);
		System.out.println(new Gson().fromJson(new String(data), ZuulProperties.ZuulRoute.class));
	}

	@org.junit.Test
	public void testAddRoute() throws Exception {
		ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
		zuulRoute.setId("user");
		zuulRoute.setPath("/user/**");
		zuulRoute.setStripPrefix(false);
		zuulRoute.setServiceId("user-service");

		addChildren("/" + zuulRoute.getId());
		setValue(zuulRoute);
		getValue("/" + zuulRoute.getId());
	}

	@org.junit.Test
	public void getChildren() throws Exception {
		for (String s : curatorClient.getChildren().forPath(ROOT_PATH)) {
			System.out.println(s);
		}
	}

}
