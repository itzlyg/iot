package cn.sinozg.applet.biz.notify.model.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 14:42
 */
@Getter
@Setter
public class NotifierWebHookConfig extends NotifierConfigBaseInfo {

    @Schema(description = "URL地址")
    @NotBlank(message = "URL地址不能为空！")
    private String hookUrl;
}
