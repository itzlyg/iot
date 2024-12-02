package cn.sinozg.applet.biz.notify.timer;

import cn.sinozg.applet.biz.notify.core.AlerterContext;
import cn.sinozg.applet.biz.notify.dispatch.MetricsTaskDispatch;
import cn.sinozg.applet.biz.notify.model.collect.Configmap;
import cn.sinozg.applet.biz.notify.model.collect.MetricsInfo;
import cn.sinozg.applet.biz.notify.model.collect.TimerJobInfo;
import cn.sinozg.applet.biz.notify.util.CollectUtil;
import cn.sinozg.applet.common.utils.CypherUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.SpringUtil;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 18:49:45
 */
@Slf4j
public class WheelTimerTask implements TimerTask {

    private final TimerJobInfo job;
    private final MetricsTaskDispatch metricsTaskDispatch;
    public WheelTimerTask(TimerJobInfo job) {
        this.metricsTaskDispatch = SpringUtil.getBean(MetricsTaskDispatch.class);
        this.job = job;
        initJobMetrics(job);
    }

    /**
     * Initialize job fill information
     * @param job job
     */
    private void initJobMetrics(TimerJobInfo job) {
        List<Configmap> config = job.getConfigmap();
        config = PojoUtil.toList(config, null, c -> {
            // decode password
            if (c.getType() == AlerterContext.PARAM_TYPE_PASSWORD && c.getValue() != null) {
                String decodeValue = CypherUtil.encoder(String.valueOf(c.getValue()));
                if (decodeValue == null) {
                    log.error("Aes Decode value {} error.", c.getValue());
                }
                c.setValue(decodeValue);
            } else if (c.getValue() != null && c.getValue() instanceof String) {
                c.setValue(((String) c.getValue()).trim());
            }
        });
        Map<String, Configmap> configmap = PojoUtil.toMap(config, Configmap::getKey);
        List<MetricsInfo> metrics = job.getMetrics();
        List<MetricsInfo> metricsTmp = new ArrayList<>(metrics.size());
        for (MetricsInfo metric : metrics) {
            metric = CollectUtil.replaceSmilingPlaceholder(metric, configmap);
            if ("push".equals(job.getApp())) {
                CollectUtil.replaceFieldsForPushStyleMonitor(metric, configmap);
            }
            metricsTmp.add(metric);
        }
        job.setMetrics(metricsTmp);
    }


    @Override
    public void run(Timeout timeout) throws Exception {
        job.setDispatchTime(System.currentTimeMillis());
        metricsTaskDispatch.dispatchMetricsTask(timeout);
    }

    public TimerJobInfo getJob() {
        return job;
    }
}
