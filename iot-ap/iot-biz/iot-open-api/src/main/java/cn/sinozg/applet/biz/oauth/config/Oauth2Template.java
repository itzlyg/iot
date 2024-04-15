package cn.sinozg.applet.biz.oauth.config;

import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.sinozg.applet.biz.oauth.service.OauthClientService;
import cn.sinozg.applet.biz.oauth.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * oauth 2配置
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-08 12:21
 */
@Slf4j
@Component
public class Oauth2Template extends SaOAuth2Template {
    @Resource
    private OauthService oauthService;
    @Resource
    private OauthClientService oauthClientService;

    @Override
    public SaClientModel getClientModel(String clientId) {
        return oauthClientService.clientModel(clientId);
    }

    @Resource
    public void setSaOauth2Config(SaOAuth2Config cfg) {
        cfg.setDoLoginHandle(oauthService.oauth2Password())
                .setIsPassword(true);
    }
}
