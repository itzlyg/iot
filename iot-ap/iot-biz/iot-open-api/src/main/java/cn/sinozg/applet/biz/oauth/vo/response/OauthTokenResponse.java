package cn.sinozg.applet.biz.oauth.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 密码模式获取到openApi token
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-09 14:13
 */
@Data
public class OauthTokenResponse {

    @Schema(description = "客户端id")
    private String clientId;

    @Schema(description = "accessToken")
    private String accessToken;

    @Schema(description = "refreshToken")
    private String refreshToken;
    @Schema(description = "有效期")
    private long expiresIn;

    @Schema(description = "refreshToken 有效期")
    private long refreshExpiresIn;
}
