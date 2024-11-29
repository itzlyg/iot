package cn.sinozg.applet.biz.notify.handle.impl;

import cn.sinozg.applet.biz.notify.core.TagsContext;
import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.biz.notify.handle.NotifierAlerterHandle;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierSmsConfig;
import cn.sinozg.applet.biz.notify.service.SmsSendService;
import cn.sinozg.applet.common.exception.CavException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 17:29
 */
@Slf4j
@Service
public class SmsNotifierAlerterHandleImpl implements NotifierAlerterHandle<NotifierSmsConfig> {

    @Resource
    private SmsSendService smsSendService;
    @Override
    public void send(NotifierSmsConfig params, NotifierTemplateInfo notifyMessage, NotifierAlerterInfo alert) {
        String monitorName = null;
        Map<String, String> tags = alert.getTags();
        if (MapUtils.isNotEmpty(tags)) {
            monitorName = MapUtils.getString(tags, TagsContext.TAG_MONITOR_NAME);
        }
        Map<String, String> paramsMap = new HashMap<>(16);
        paramsMap.put("monitorName", StringUtils.defaultString(monitorName, alert.getTarget()));
        paramsMap.put("priority", alert.getPriorityName());
        paramsMap.put("content", alert.getContent());
        try {
            smsSendService.sendMessage(alert.getToUser(), paramsMap);
        } catch (Exception e) {
            throw new CavException("发送短信预警失败！ " , e);
        }
    }

    @Override
    public NotifierType notifyType() {
        return NotifierType.SMS;
    }
}
