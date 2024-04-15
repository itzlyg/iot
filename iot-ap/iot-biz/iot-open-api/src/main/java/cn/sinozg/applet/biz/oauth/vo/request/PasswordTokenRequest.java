package cn.sinozg.applet.biz.oauth.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 密码模式获取到openApi token
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-09 14:13
 */
@Data
public class PasswordTokenRequest {

    @Schema(description = "应用id")
    @NotBlank(message = "应用id不能为空！")
    private String clientId;

    @Schema(description = "应用秘钥")
    @NotBlank(message = "应用秘钥不能为空！")
    private String clientSecret;
}
