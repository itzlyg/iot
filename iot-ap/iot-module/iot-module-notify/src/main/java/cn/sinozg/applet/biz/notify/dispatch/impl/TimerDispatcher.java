package cn.sinozg.applet.biz.notify.dispatch.impl;

import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;
import cn.sinozg.applet.biz.notify.dispatch.TimerDispatch;
import cn.sinozg.applet.biz.notify.timer.WheelTimerTask;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 任务调度器
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 19:21:13
 */
@Slf4j
@Component
public class TimerDispatcher implements TimerDispatch, DisposableBean {

    /**
     * time round schedule
     */
    private final Timer wheelTimer;
    /**
     * Existing periodic scheduled tasks
     */
    private final Map<String, Timeout> currentCyclicTaskMap;
    /**
     * Existing temporary scheduled tasks
     */
    private final Map<String, Timeout> currentTempTaskMap;
    
    /**
     * is dispatcher online running
     */
    private final AtomicBoolean started;
    
    public TimerDispatcher() {
        this.wheelTimer = new HashedWheelTimer(r -> {
            Thread ret = new Thread(r, "wheelTimer");
            ret.setDaemon(true);
            return ret;
        }, 1, TimeUnit.SECONDS, 512);
        this.currentCyclicTaskMap = new ConcurrentHashMap<>(8);
        this.currentTempTaskMap = new ConcurrentHashMap<>(8);
        this.started = new AtomicBoolean(true);
    }


    @Override
    public void cyclicJob(WheelTimerTask timerTask, long interval, TimeUnit timeUnit) {
        if (!this.started.get()) {
            log.warn("Collector is offline, can not dispatch collect jobs.");
            return;
        }
        String jobId = timerTask.getJob().getId();
        // whether is the job has been canceled
        if (currentCyclicTaskMap.containsKey(jobId)) {
            Timeout timeout = wheelTimer.newTimeout(timerTask, interval, TimeUnit.SECONDS);
            currentCyclicTaskMap.put(timerTask.getJob().getId(), timeout);
        }
    }

    @Override
    public void deleteJob(String jobId, boolean isCyclic) {
        Timeout timeout;
        if (isCyclic) {
            timeout = currentCyclicTaskMap.remove(jobId);
        } else {
            timeout = currentTempTaskMap.remove(jobId);
        }
        if (timeout != null) {
            timeout.cancel();
        }
    }
    
    @Override
    public void goOnline() {
        currentCyclicTaskMap.forEach((key, value) -> value.cancel());
        currentCyclicTaskMap.clear();
        currentTempTaskMap.forEach((key, value) -> value.cancel());
        currentTempTaskMap.clear();
        started.set(true);
    }
    
    @Override
    public void goOffline() {
        started.set(false);
        currentCyclicTaskMap.forEach((key, value) -> value.cancel());
        currentCyclicTaskMap.clear();
        currentTempTaskMap.forEach((key, value) -> value.cancel());
        currentTempTaskMap.clear();
    }
    
    
    @Override
    public void responseSyncJobData(String jobId, List<CollectMetricsParams> metricsDataTemps) {
        currentTempTaskMap.remove(jobId);
    }
    
    @Override
    public void destroy() throws Exception {
        this.wheelTimer.stop();
    }
}
