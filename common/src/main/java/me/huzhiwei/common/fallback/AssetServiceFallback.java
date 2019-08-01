package me.huzhiwei.common.fallback;

import me.huzhiwei.common.service.AssetService;
import org.springframework.stereotype.Component;

@Component
public class AssetServiceFallback implements AssetService {

	@Override
	public String getAsset(String userId) {
		return "asset服务挂了";
	}
}
