package cn.sinozg.applet.biz.notify.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 机器人返回
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-19 18:04:58
 */
@Data
public class RobotNotifyResponse {

    @JsonProperty(value = "errcode")
    private Integer errorCode;
    @JsonProperty(value = "errmsg")
    private String errorMsg;

    private Integer code;

    private String msg;
}
