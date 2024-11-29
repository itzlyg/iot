package cn.sinozg.applet.biz.notify.service.impl;

import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;
import cn.sinozg.applet.biz.notify.service.AlerterDataQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 内存的方式实现队列
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 15:05
 */
@Slf4j
@Service
public class MemoryAlerterDataQueueServiceImpl implements AlerterDataQueueService {

    private final LinkedBlockingQueue<NotifierAlerterInfo> alerterDataQueue;
    private final LinkedBlockingQueue<CollectMetricsParams> metricsDataToAlerterQueue;

    public MemoryAlerterDataQueueServiceImpl(){
        alerterDataQueue = new LinkedBlockingQueue<>();
        metricsDataToAlerterQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void sendAlerterData(NotifierAlerterInfo alert) {
        alerterDataQueue.offer(alert);
    }

    @Override
    public NotifierAlerterInfo pollAlerterData() throws InterruptedException {
        return alerterDataQueue.poll(2, TimeUnit.SECONDS);
    }

    @Override
    public void sendMetricsData(CollectMetricsParams metricsData) {
        metricsDataToAlerterQueue.offer(metricsData);
    }

    @Override
    public CollectMetricsParams pollMetricsDataToAlerter() throws InterruptedException {
        return metricsDataToAlerterQueue.poll(2, TimeUnit.SECONDS);
    }
}
