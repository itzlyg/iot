package cn.sinozg.applet.biz.notify.handle.impl;

import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.biz.notify.handle.BaseNotifierAlerterHandle;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierWebHookConfig;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 21:08
 */
@Slf4j
@Service
public class WebHookNotifierAlerterHandleImpl extends BaseNotifierAlerterHandle<NotifierWebHookConfig> {
    @Override
    public void send(NotifierWebHookConfig params, NotifierTemplateInfo notifyTemplate, NotifierAlerterInfo alert) {
        // fix null pointer exception
        filterInvalidTags(alert);
        String json = renderContent(notifyTemplate, alert);
        json = json.replace(",\n  }", "\n }");
        String result = HttpUtil.doPost(params.getHookUrl(), null, json);
        if (StringUtils.isBlank(result)) {
            throw new CavException("发送webHook通知失败！");
        }
    }

    @Override
    public NotifierType notifyType() {
        return NotifierType.WEB_HOOK;
    }

    private void filterInvalidTags(NotifierAlerterInfo alert) {
        Map<String, String> tags = alert.getTags();
        if (MapUtils.isEmpty(tags)) {
            return;
        }
        Iterator<Map.Entry<String, String>> iterator = alert.getTags().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (StringUtils.isNoneBlank(entry.getKey(), entry.getValue())) {
                continue;
            }
            iterator.remove();
        }
        // In order to beautify Freemarker template
        if (alert.getTags().entrySet().size() <= 0L) {
            alert.setTags(null);
        }
    }
}
