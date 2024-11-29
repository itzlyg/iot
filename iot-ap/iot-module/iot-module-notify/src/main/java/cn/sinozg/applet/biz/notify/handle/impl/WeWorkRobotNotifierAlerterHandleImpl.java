package cn.sinozg.applet.biz.notify.handle.impl;

import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.biz.notify.handle.BaseNotifierAlerterHandle;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierWwRobotConfig;
import cn.sinozg.applet.biz.notify.model.response.RobotNotifyResponse;
import cn.sinozg.applet.biz.notify.model.ww.WwContent;
import cn.sinozg.applet.biz.notify.model.ww.WwRobotModel;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 企业微信机器人
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 20:08
 */
@Slf4j
@Service
public class WeWorkRobotNotifierAlerterHandleImpl extends BaseNotifierAlerterHandle<NotifierWwRobotConfig> {
    @Override
    public void send(NotifierWwRobotConfig params, NotifierTemplateInfo notifyTemplate, NotifierAlerterInfo alert) {
        WwRobotModel model = new WwRobotModel();
        WwContent markdown = new WwContent();
        String content = renderContent(notifyTemplate, alert);
        markdown.setContent(content);
        model.setMarkdown(markdown);
        String url = this.properties.getWwRobotUrl() + params.getOpenId();
        RobotNotifyResponse response = HttpUtil.doPost(url, null, model, RobotNotifyResponse.class);
        if (response == null || response.getCode() != 0) {
            throw new CavException("发送企业消息失败！");
        }
    }

    @Override
    public NotifierType notifyType() {
        return NotifierType.WE_WORK_ROBOT;
    }
}
