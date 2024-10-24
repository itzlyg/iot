package cn.sinozg.applet.biz.oauth.config;

import cn.dev33.satoken.oauth2.config.SaOAuth2ServerConfig;
import cn.dev33.satoken.oauth2.data.model.loader.SaClientModel;
import cn.sinozg.applet.biz.oauth.service.OauthClientService;
import cn.sinozg.applet.biz.oauth.service.OauthService;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * oauth 2配置
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-08 12:21
 */
@Slf4j
@Component
public class SaOauth2Config {

    @Resource
    private OauthService oauthService;
    @Resource
    private OauthClientService oauthClientService;

    @Resource
    public void setSaOauth2Config(SaOAuth2ServerConfig cfg) {
        List<SaClientModel> models = oauthClientService.clientModel();
        for (SaClientModel model : models) {
            cfg.addClient(model);
        }
        cfg.doLoginHandle = oauthService.oauth2Password();
        cfg.setEnablePassword(true);
    }
}
