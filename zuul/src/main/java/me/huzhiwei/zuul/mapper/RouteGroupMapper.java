package me.huzhiwei.zuul.mapper;

import me.huzhiwei.zuul.domain.RouteGroup;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-08 19:30
 */
@Mapper
public interface RouteGroupMapper {

	@Insert("insert into route_group (name, online) values (#{name}, #{online})")
	@SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = String.class)
	int insert(RouteGroup routeGroup);

	@Select("select id, name, online from route_group")
	List<RouteGroup> selectAllRouteGroups();
}
