package me.huzhiwei.zuul.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-16 13:43
 */
@Data
public class Role implements GrantedAuthority {

    private Long id;
    private String name;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
