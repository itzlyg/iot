package cn.sinozg.applet.biz.notify.model.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 接收人
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 14:42
 */
@Getter
@Setter
public class NotifierWwAppConfig extends NotifierConfigBaseInfo {

    @Schema(description = "企业信息 : corpId")
    @NotBlank(message = "企业corpId不能为空！")
    private String corpId;

    @Schema(description = "企业微信应用id")
    @NotNull(message = "应用id不能为空！")
    private Integer agentId;

    @Schema(description = "企业微信应用secret")
    @NotBlank(message = "应用secret不能为空！")
    private String appSecret;
}
