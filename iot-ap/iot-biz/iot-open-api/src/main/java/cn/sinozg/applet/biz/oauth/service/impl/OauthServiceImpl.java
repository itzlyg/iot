package cn.sinozg.applet.biz.oauth.service.impl;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.oauth2.consts.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.data.model.loader.SaClientModel;
import cn.dev33.satoken.oauth2.function.SaOAuth2DoLoginHandleFunction;
import cn.dev33.satoken.oauth2.template.SaOAuth2Util;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.sinozg.applet.biz.oauth.content.OauthContent;
import cn.sinozg.applet.biz.oauth.service.OauthClientService;
import cn.sinozg.applet.biz.oauth.service.OauthService;
import cn.sinozg.applet.biz.oauth.vo.response.OauthTokenResponse;
import cn.sinozg.applet.biz.system.service.UserInfoService;
import cn.sinozg.applet.common.core.model.LoginUserVo;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.HttpUtil;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-09 14:15
 */
@Slf4j
@Service
public class OauthServiceImpl implements OauthService {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ServerProperties serverProperties;
    @Resource
    private OauthClientService oauthClientService;

    @Override
    public SaOAuth2DoLoginHandleFunction oauth2Password() {
        return (u, p) -> {
            SaRequest request = SaHolder.getRequest();
            Map<String, String> map = request.getParamMap();
            String tp = MapUtils.getString(map, OauthContent.TP);
            String clientId = map.get(SaOAuth2Consts.Param.client_id);
            SaClientModel model = SaOAuth2Util.checkClientModel(clientId);
            String userId = oauthClientService.userIdByClient(model.getClientId(), model.getClientSecret());
            // 用户 密码
            boolean pass = pass(userId, tp, u, p);
            if (pass) {
                StpUtil.login(userId);
                return SaResult.ok();
            }
            throw new CavException("密码不正确！");
        };
    }

    /**
     * 获取或刷新Token
     * 如果提供的token为空，则尝试获取新的access token；如果提供的是refresh token，则尝试刷新access token。
     * @param clientId 客户端ID
     * @param clientSecret 客户端秘钥
     * @param token 提供的token，可以是access token或refresh token
     * @return 返回包含token信息的响应对象
     */
    @Override
    public OauthTokenResponse oauthToken (String clientId, String clientSecret, String token){
        String error = "刷新token错误！";
        String format = OauthContent.REFRESH_URL;
        if (StringUtils.isBlank(token)) {
            error = "获取token错误！";
            format = OauthContent.TOKEN_URL;
            token = OauthContent.TP_FLAG;
        }
        String url = String.format(format, serverProperties.getPort(), clientId, clientSecret, token);
        String result = HttpUtil.doGet(url);
        SaResult saResult = JsonUtil.toPojo(result, SaResult.class);
        if (saResult != null) {
            if (saResult.getCode() == SaResult.CODE_SUCCESS) {
                Object o = saResult.getData();
                if (o instanceof Map) {
                    Map<String, Object> map = PojoUtil.cast(o);
                    OauthTokenResponse response = new OauthTokenResponse();
                    response.setClientId(MapUtils.getString(map, OauthContent.CLIENT_ID));
                    response.setAccessToken(MapUtils.getString(map, OauthContent.ACCESS_TOKEN));
                    response.setRefreshToken(MapUtils.getString(map, OauthContent.REFRESH_TOKEN));
                    response.setExpiresIn(MapUtils.getLong(map, OauthContent.EXPIRES_IN));
                    response.setRefreshExpiresIn(MapUtils.getLong(map, OauthContent.REFRESH_EXPIRES_IN));
                    String uid = oauthClientService.userIdByClient(clientId, clientSecret);
                    StpUtil.login(uid, SaLoginConfig.setToken(response.getAccessToken()));
                    return response;
                }
            } else {
                log.error("{}，{}", error, result);
                error = saResult.getMsg();
            }
        }
        throw new CavException(error);
    }

    /**
     * 是否通过
     * @param userId userId
     * @param tp tp
     * @param u u
     * @param p p
     * @return 是否通过
     */
    private boolean pass (String userId, String tp, String u, String p){
        if (!StringUtils.equals(OauthContent.TP_FLAG, tp)) {
            if (!StringUtils.equals(userId, u)) {
                return false;
            }
            LoginUserVo user = userInfoService.passwordInfo(userId);
            return user != null && StringUtils.equals(p, user.getOpenId());
        }
        return true;
    }
}
