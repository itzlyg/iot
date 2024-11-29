package cn.sinozg.applet.biz.notify.handle.impl;

import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.biz.notify.handle.BaseNotifierAlerterHandle;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierDingTalkConfig;
import cn.sinozg.applet.biz.notify.model.ding.DingMarkdown;
import cn.sinozg.applet.biz.notify.model.ding.DingTalkRobotModel;
import cn.sinozg.applet.biz.notify.model.response.RobotNotifyResponse;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 钉钉机器人消息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 17:5
 */
@Slf4j
@Service
public class DingTalkRobotNotifierAlerterHandleImpl extends BaseNotifierAlerterHandle<NotifierDingTalkConfig> {
    @Override
    public void send(NotifierDingTalkConfig params, NotifierTemplateInfo notifyTemplate, NotifierAlerterInfo alert) {
        DingTalkRobotModel model = new DingTalkRobotModel();
        DingMarkdown markdown = new DingMarkdown();
        String content = renderContent(notifyTemplate, alert);
        markdown.setContent(content);
        model.setMarkdown(markdown);
        String url = this.properties.getDingTalkUrl() + params.getAccessToken();
        RobotNotifyResponse response = HttpUtil.doPost(url, null, model, RobotNotifyResponse.class);
        if (response == null || response.getCode() != 0) {
            throw new CavException("发送钉钉消息失败！");
        }
    }

    @Override
    public NotifierType notifyType() {
        return NotifierType.DING_TALK_ROBOT;
    }
}
