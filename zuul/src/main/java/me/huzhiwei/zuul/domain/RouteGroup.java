package me.huzhiwei.zuul.domain;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class RouteGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @KeySql(useGeneratedKeys = true)
    private String id;
    private String name;
    private Boolean online;
    private String description;

    public RouteGroup() {}

    public RouteGroup(String id) {
        this.id = id;
    }
}
