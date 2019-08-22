package me.huzhiwei.zuul.domain;

import com.ecwid.consul.v1.agent.model.Service;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-22 14:55
 */
@Data
public class ServiceWithCheck extends Service {

    @SerializedName("Output")
    private String output;

    @SerializedName("Status")
    private String status;
}
