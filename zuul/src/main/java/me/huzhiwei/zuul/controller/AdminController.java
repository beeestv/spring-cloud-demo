package me.huzhiwei.zuul.controller;

import me.huzhiwei.zuul.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-28 19:57
 */
@RestController
@RequestMapping("/gateway")
@Validated
public class AdminController {

    @Autowired
    private InMemoryTokenStore tokenStore;

    @PostMapping("/admin/logout")
    public Result logout(@RequestParam("access_token") String accessToken) {
        tokenStore.removeAccessToken(accessToken);
        return Result.success();
    }
}
