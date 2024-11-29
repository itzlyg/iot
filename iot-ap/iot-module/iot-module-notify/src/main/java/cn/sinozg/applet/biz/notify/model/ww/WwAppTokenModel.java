package cn.sinozg.applet.biz.notify.model.ww;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 20:32
 */
@Data
public class WwAppTokenModel {

    @JsonProperty(value = "errcode")
    private Integer errCode;

    @JsonProperty(value = "errmsg")
    private String errMsg;

    @JsonProperty(value = "access_token")
    private String accessToken;
}
