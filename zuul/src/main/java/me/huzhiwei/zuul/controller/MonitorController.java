package me.huzhiwei.zuul.controller;

import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.domain.Result;
import me.huzhiwei.zuul.mapper.RequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-12 15:10
 */
@RestController
@RequestMapping("/monitor")
@Slf4j
public class MonitorController {

    @Autowired
    private RequestMapper requestMapper;

    @GetMapping
    public Result overview() {
        return Result.success();
    }
}
