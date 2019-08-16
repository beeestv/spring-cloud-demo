package me.huzhiwei.zuul.service;

import me.huzhiwei.zuul.domain.Role;
import me.huzhiwei.zuul.domain.User;
import me.huzhiwei.zuul.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-16 13:40
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", username);
        User user = userMapper.selectOneByExample(example);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        List<Role> authorities = userMapper.getAuthorities(user.getId());
        user.setAuthorities(authorities);
        return user;
    }
}
