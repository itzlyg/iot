package cn.sinozg.applet.biz.notify.timer;

import cn.sinozg.applet.biz.notify.execute.AlerterFactory;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierRuleInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierConfigBaseInfo;
import cn.sinozg.applet.biz.notify.service.AlerterDataQueueService;
import cn.sinozg.applet.biz.notify.service.AlerterDataService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-21 16:37
 */
@Slf4j
public class AlerterTask implements Runnable {
    private final AlerterDataQueueService dataQueueService;
    private final AlerterDataService alerterDataService;

    public AlerterTask(AlerterDataQueueService dataQueueService, AlerterDataService alerterDataService ){
        this.alerterDataService = alerterDataService;
        this.dataQueueService = dataQueueService;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                NotifierAlerterInfo alert = dataQueueService.pollAlerterData();
                if (alert != null) {
                    // Determining alarm type storage   判断告警类型入库
                    alerterDataService.saveAlerter(alert);
                    // 通知分发
                    sendNotify(alert);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            } catch (Exception exception) {
                log.error(exception.getMessage(), exception);
            }

        }
    }

    private void sendNotify(NotifierAlerterInfo alert) {
        List<NotifierRuleInfo> rules = alerterDataService.matchRulesByAlerter(alert);
        // todo Send notification here temporarily single thread     发送通知这里暂时单线程
        if (CollectionUtils.isNotEmpty(rules)) {
            for (NotifierRuleInfo rule : rules) {
                try {
                    NotifierConfigBaseInfo baseInfo = alerterDataService.configById(rule.getConfigId());
                    NotifierTemplateInfo templateInfo = null;
                    if (rule.getTemplateId() != null) {
                        templateInfo = alerterDataService.templateById(rule.getTemplateId());
                    }
                    boolean send = AlerterFactory.sendNoticeMsg(baseInfo, templateInfo, alert);
                    if (!send) {
                        log.error("发送告警信息错误!!!{}", alert);
                    }
                } catch (Exception e) {
                    log.warn("发送通知错误, message: {}", e.getMessage());
                }
            }
        }
    }
}
