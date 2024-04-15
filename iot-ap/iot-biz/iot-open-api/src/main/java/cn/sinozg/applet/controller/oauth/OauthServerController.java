package cn.sinozg.applet.controller.oauth;

import cn.dev33.satoken.oauth2.logic.SaOAuth2Handle;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务配置
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-09 11:32
 */
@Slf4j
@Hidden
@RestController
@Tag(name = "oauth-server-controller", description = "oauth2 授权相关")
public class OauthServerController {
    @RequestMapping("/oauth2/*")
    public Object request() {
        return SaOAuth2Handle.serverRequest();
    }
}
