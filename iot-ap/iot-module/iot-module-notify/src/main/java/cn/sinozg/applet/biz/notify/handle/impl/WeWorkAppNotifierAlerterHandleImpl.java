package cn.sinozg.applet.biz.notify.handle.impl;

import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.biz.notify.handle.BaseNotifierAlerterHandle;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierWwAppConfig;
import cn.sinozg.applet.biz.notify.model.response.RobotNotifyResponse;
import cn.sinozg.applet.biz.notify.model.ww.WwAppModel;
import cn.sinozg.applet.biz.notify.model.ww.WwAppTokenModel;
import cn.sinozg.applet.biz.notify.model.ww.WwContent;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.HttpUtil;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * 企业微信app
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 20:08
 */
@Slf4j
@Service
public class WeWorkAppNotifierAlerterHandleImpl extends BaseNotifierAlerterHandle<NotifierWwAppConfig> {
    @Override
    public void send(NotifierWwAppConfig params, NotifierTemplateInfo notifyTemplate, NotifierAlerterInfo alert) {
        String corpId = params.getCorpId();
        Integer agentId = params.getAgentId();
        String appSecret = params.getAppSecret();
        String accessToken = accessToken(corpId, appSecret);
        // 所有人
        String toUser = "@all";
        List<String> list = alert.getToUser();
        if (CollectionUtils.isNotEmpty(list)) {
            toUser = StringUtils.join(list, "|");
        }
        WwContent markdown = new WwContent();
        markdown.setContent(renderContent(notifyTemplate, alert));
        WwAppModel model = new WwAppModel();

        model.setToUser(toUser);
        model.setMarkdown(markdown);
        model.setAgentId(agentId);
        String url = String.format(this.properties.getWwAppUrl(), accessToken);
        RobotNotifyResponse response = HttpUtil.doPost(url, null, model, RobotNotifyResponse.class);
        if (response == null || response.getCode() != 0) {
            throw new CavException("发送企业微信应用消息失败！");
        }

    }

    @Override
    public NotifierType notifyType() {
        return NotifierType.WE_WORK_ROBOT;
    }

    /**
     * 获取到 accessToken
     * @param corpId  corpId
     * @param appSecret appSecret
     * @return accessToken
     */
    private String accessToken (String corpId, String appSecret){
        String key = String.format(RedisKey.WW_ACCESS_TOKEN, corpId);
        String accessToken = RedisUtil.getCacheObject(key);
        if (StringUtils.isBlank(accessToken)) {
           String result = HttpUtil.doGet(String.format(properties.getWwSecretUrl(), corpId, appSecret));
            if (StringUtils.isNotBlank(result)) {
                WwAppTokenModel model = JsonUtil.toPojo(result, WwAppTokenModel.class);
                if (model != null) {
                    accessToken = model.getAccessToken();
                }
            }
            if (StringUtils.isBlank(accessToken)) {
                log.error("企业微信返回的信息为:{}", result);
                throw new CavException("获取企业微信token失败！");
            }
            // todo 确定有效时间
            RedisUtil.setCacheObject(key, accessToken, Duration.ofDays(1));
        }
        return accessToken;
    }
}
