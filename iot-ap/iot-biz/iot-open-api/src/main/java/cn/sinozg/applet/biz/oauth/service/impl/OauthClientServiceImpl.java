package cn.sinozg.applet.biz.oauth.service.impl;

import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.sinozg.applet.biz.oauth.entity.OauthClient;
import cn.sinozg.applet.biz.oauth.mapper.OauthClientMapper;
import cn.sinozg.applet.biz.oauth.service.OauthClientService;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
* 授权客户端 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-09 12:54:47
*/
@Service
public class OauthClientServiceImpl extends ServiceImpl<OauthClientMapper, OauthClient> implements OauthClientService {

    @Resource
    private OauthClientMapper mapper;

    @Override
    public SaClientModel clientModel(String clientId) {
        OauthClient c = clientCache(clientId);
        SaClientModel clientModel = new SaClientModel()
                .setClientId(c.getClientId())
                .setClientSecret(c.getClientSecret())
                .setAllowUrl(c.getAllowUrl())
                .setAccessTokenTimeout(c.getAccessTokenTimeout())
                .setRefreshTokenTimeout(c.getRefreshTokenTimeout())
                .setContractScope(c.getScopes());
        if (StringUtils.contains(c.getMode(), SaOAuth2Consts.GrantType.password)) {
            clientModel.setIsPassword(true);
        } else if (StringUtils.contains(c.getMode(), SaOAuth2Consts.GrantType.authorization_code)) {
            clientModel.setIsCode(true);
        }
        return clientModel;
    }

    @Override
    public String userIdByClient(String clientId, String clientSecret) {
        OauthClient client = clientCache(clientId);
        if (clientSecret.equals(client.getClientSecret())) {
            return client.getBindUser();
        }
        throw new CavException("客户端信息错误！");
    }


    /**
     * 缓存获取 client 信息
     * @param clientId clientId
     * @return client 信息
     */
    private OauthClient clientCache (String clientId){
        String redisKye = String.format(RedisKey.OAUTH_CLIENT, clientId);
        OauthClient client = RedisUtil.getCacheObject(redisKye);
        if (client != null) {
            return client;
        }
        List<OauthClient> list = this.list(new LambdaQueryWrapper<OauthClient>()
                .eq(OauthClient::getClientId, clientId));
        if (PojoUtil.single(list)) {
            client = list.get(0);
            RedisUtil.setCacheObject(redisKye, client, Duration.ofDays(1));
            return client;
        }
        throw new CavException("客户端信息{}不存在！", clientId);
    }
}
