package me.huzhiwei.zuul.domain;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-07 16:12
 */
@Data
public class Route implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @KeySql(useGeneratedKeys = true)
    private String id;
    private String name;
    private String path;
    private String serviceId;
    private String url;
    @Column
    private Boolean stripPrefix = false;
    private String groupId;

    public Route() {}

    public Route(String id) {
        this.groupId = id;
    }
}
