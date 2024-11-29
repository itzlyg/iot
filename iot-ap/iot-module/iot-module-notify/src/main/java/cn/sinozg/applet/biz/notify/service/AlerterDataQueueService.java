package cn.sinozg.applet.biz.notify.service;

import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 14:51
 */
public interface AlerterDataQueueService {

    /**
     * 发送告警数据
     * @param alert 告警
     */
    void sendAlerterData(NotifierAlerterInfo alert);

    /**
     * 消费告警数据
     * @return 数据
     */
    NotifierAlerterInfo pollAlerterData() throws InterruptedException;

    /**
     * 数据收集 发送告警数据
     * 开启延迟线程收集到 任务的数据
     * 周期性任务
     * @param metricsData metrics data
     */
    void sendMetricsData(CollectMetricsParams metricsData);

    /**
     * 消费数据收集的告警信息
     * 通过计算的方式，判断级别 ，转化器过滤、场景过滤后
     * 发送到 sendAlerterData 队列。
     * @return metrics data
     * @throws InterruptedException when poll timeout
     */
    CollectMetricsParams pollMetricsDataToAlerter() throws InterruptedException;
}
