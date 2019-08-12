package me.huzhiwei.zuul.controller;

import lombok.extern.slf4j.Slf4j;
import me.huzhiwei.zuul.domain.Request;
import me.huzhiwei.zuul.domain.RequestQuery;
import me.huzhiwei.zuul.domain.Result;
import me.huzhiwei.zuul.domain.Route;
import me.huzhiwei.zuul.mapper.RequestMapper;
import me.huzhiwei.zuul.mapper.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Autowired
    private RouteMapper routeMapper;

    @GetMapping
    public Result overview(RequestQuery query) {
        Example example = null;
        /**
         * 如果搜索条件有groupId，要先查出group下的route ids
         * TODO request表增加group id字段，避免这次查询步骤
         */
        List<String> routeIds = null;
        if (Objects.nonNull(query.getGroupId())) {
            example = new Example(Route.class);
            example.createCriteria().andCondition("groupId", query.getGroupId());
            List<Route> routeList = routeMapper.selectByExample(example);
            routeIds = routeList.stream().map(Route::getGroupId).collect(Collectors.toList());
        }

        example = new Example(Request.class);
        Example.Criteria criteria = example.createCriteria();
        if (!CollectionUtils.isEmpty(routeIds)) {
            criteria.andIn("routeId", routeIds);
        }
        if (Objects.nonNull(query.getRouteId())) {
            criteria.andCondition("routeId", query.getRouteId());
        }
        if (Objects.nonNull(query.getBeginTime())) {
            if (Objects.isNull(query.getEndTime())) {
                query.setEndTime(System.currentTimeMillis());
            }
            criteria.andBetween("requestTime", query.getBeginTime(), query.getEndTime());
        }

        Map<String, Object> overview = new HashMap<>();
        overview.put("count", requestMapper.selectCountByExample(example));
        return Result.success(overview);
    }

}
