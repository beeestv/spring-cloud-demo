package me.huzhiwei.zuul.domain;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-16 13:41
 */
@Data
public class User implements UserDetails {
    private Long id;
    private String username;
    private String password;

    private List<Role> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
