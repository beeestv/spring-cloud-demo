package me.huzhiwei.zuul.mapper;

import me.huzhiwei.zuul.domain.Request;
import me.huzhiwei.zuul.domain.RequestQuery;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-12 14:01
 */
public interface RequestMapper extends Mapper<Request> {

    Map<String, Object> overview(RequestQuery query);
}
