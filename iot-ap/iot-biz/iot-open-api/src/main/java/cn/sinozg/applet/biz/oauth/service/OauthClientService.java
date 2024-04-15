package cn.sinozg.applet.biz.oauth.service;

import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.sinozg.applet.biz.oauth.entity.OauthClient;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 授权客户端 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-09 12:54:47
*/
public interface OauthClientService extends IService<OauthClient> {

    /**
     * 获取 客户端
     * @param clientId clientId
     * @return 客户端信息
     */
    SaClientModel clientModel(String clientId);

    /**
     * 根据客户端信息获取到 绑定用户
     * @param clientId clientId
     * @param clientSecret clientSecret
     * @return 绑定用户
     */
    String userIdByClient(String clientId, String clientSecret);
}
