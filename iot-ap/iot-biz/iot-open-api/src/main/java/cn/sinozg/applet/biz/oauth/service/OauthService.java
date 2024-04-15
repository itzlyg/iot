package cn.sinozg.applet.biz.oauth.service;

import cn.sinozg.applet.biz.oauth.vo.response.OauthTokenResponse;

import java.util.function.BiFunction;

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
    BiFunction<String, String, Object> oauth2Password();

    /**
     * 获取token 或者刷新token
     * @param clientId clientId
     * @param clientSecret clientSecret
     * @param token refresh_token
     * @return token信息
     */
    OauthTokenResponse oauthToken (String clientId, String clientSecret, String token);
}
