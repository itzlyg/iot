package cn.sinozg.applet.biz.oauth.service.impl;

import cn.dev33.satoken.oauth2.config.SaOAuth2ServerConfig;
import cn.dev33.satoken.oauth2.data.model.loader.SaClientModel;
import cn.sinozg.applet.biz.oauth.service.OauthClientService;
import cn.sinozg.applet.biz.oauth.service.OauthService;
import cn.sinozg.applet.common.service.FrameworkInitRunService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-10-27 13:23
 */
@Service
public class SaOauthInitRunService implements FrameworkInitRunService {

    @Resource
    private OauthService oauthService;

    @Resource
    private OauthClientService oauthClientService;

    @Resource
    private SaOAuth2ServerConfig cfg;

    @Override
    public void initInfo() {
        List<SaClientModel> models = oauthClientService.clientModel();
        for (SaClientModel model : models) {
            cfg.addClient(model);
        }
        cfg.doLoginHandle = oauthService.oauth2Password();
        cfg.setEnablePassword(true);
    }
}
