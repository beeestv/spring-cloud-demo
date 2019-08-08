package me.huzhiwei.user;

import me.huzhiwei.common.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private AssetService assetService;

	@Autowired
	private Config config;


	@GetMapping("/asset")
	public String getAsset() {
		return assetService.getAsset(config.getName());
	}

	@GetMapping("/name")
	public String getName() {
		return config.getName();
	}
}
