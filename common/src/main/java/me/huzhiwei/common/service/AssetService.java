package me.huzhiwei.common.service;

import me.huzhiwei.common.fallback.AssetServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "asset-service", path = "/asset", fallback = AssetServiceFallback.class)
public interface AssetService {

	@GetMapping()
	String getAsset(@RequestParam String userId);
}
