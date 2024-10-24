package cn.sinozg.applet.biz.oauth.service;

import cn.dev33.satoken.oauth2.function.SaOAuth2DoLoginHandleFunction;
import cn.sinozg.applet.biz.oauth.vo.response.OauthTokenResponse;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-09 14:15
 */
public interface OauthService {

    /**
     * 秘密模式登录
     * @return 结果
     */
    SaOAuth2DoLoginHandleFunction oauth2Password();

    /**
     * 获取token 或者刷新token
     * @param clientId clientId
     * @param clientSecret clientSecret
     * @param token refresh_token
     * @return token信息
     */
    OauthTokenResponse oauthToken (String clientId, String clientSecret, String token);
}
