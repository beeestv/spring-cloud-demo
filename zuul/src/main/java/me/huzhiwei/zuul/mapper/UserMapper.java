package me.huzhiwei.zuul.mapper;

import me.huzhiwei.zuul.domain.Role;
import me.huzhiwei.zuul.domain.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-16 13:41
 */
public interface UserMapper extends Mapper<User> {

    List<Role> getAuthorities(@Param("id") Long id);

}
