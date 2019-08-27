package me.huzhiwei.zuul.domain;

import lombok.Data;
import me.huzhiwei.zuul.constant.Constant;

import javax.validation.constraints.NotBlank;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-23 15:57
 */
@Data
public class ClientAddRO {
    @NotBlank
    private String clientId;

    private String resourceIdsString = Constant.RESOURECE_ID;

    private String scopeString = "read";

    private Boolean available = false;
}
