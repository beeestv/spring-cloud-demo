package me.huzhiwei.zuul.domain;

import com.github.pagehelper.IPage;
import lombok.Data;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-22 15:29
 */
@Data
public class Page implements IPage {
    private Integer pageNum = 0;
    private Integer pageSize = 10;
    private String orderBy;
}
