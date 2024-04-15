package cn.sinozg.applet.controller.oauth;

import cn.sinozg.applet.biz.oauth.service.OauthService;
import cn.sinozg.applet.biz.oauth.vo.request.PasswordTokenRequest;
import cn.sinozg.applet.biz.oauth.vo.request.RefreshTokenRequest;
import cn.sinozg.applet.biz.oauth.vo.response.OauthTokenResponse;
import cn.sinozg.applet.common.core.base.BaseRequest;
import cn.sinozg.applet.common.core.base.BaseResponse;
import cn.sinozg.applet.common.utils.MsgUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-09 14:11
 */
@RestController
@RequestMapping("/api/anon/oauth")
@Tag(name = "oauth-controller", description = "第三方oauth2 授权相关")
public class OauthController {

    @Resource
    private OauthService oauthService;
    @PostMapping("/token")
    @Operation(summary = "获取token")
    public BaseResponse<OauthTokenResponse> tokenByPassword(@RequestBody @Valid BaseRequest<PasswordTokenRequest> request) {
        PasswordTokenRequest params = MsgUtil.params(request);
        OauthTokenResponse token = oauthService.oauthToken(params.getClientId(), params.getClientSecret(), null);
        return MsgUtil.ok(token);
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "刷新token")
    public BaseResponse<OauthTokenResponse> refreshToken(@RequestBody @Valid BaseRequest<RefreshTokenRequest> request) {
        RefreshTokenRequest params = MsgUtil.params(request);
        OauthTokenResponse token = oauthService.oauthToken(params.getClientId(), params.getClientSecret(), params.getRefreshToken());
        return MsgUtil.ok(token);
    }
}
