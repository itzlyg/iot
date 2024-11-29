package cn.sinozg.applet.biz.notify.dispatch;


import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;
import cn.sinozg.applet.biz.notify.timer.WheelTimerTask;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * timer dispatch service
 *
 * @author tomsun28
 */
public interface TimerDispatch {

    /**
     * Cyclic job
     * @param timerTask timerTask
     * @param interval  collect interval
     * @param timeUnit  time unit
     */
    void cyclicJob(WheelTimerTask timerTask, long interval, TimeUnit timeUnit);

    /**
     * Delete existing job
     * @param jobId    jobId
     * @param isCyclic Whether it is a periodic task, true is, false is a temporary task
     */
    void deleteJob(String jobId, boolean isCyclic);
    
    /**
     * job dispatcher go online
     */
    void goOnline();
    
    /**
     * job dispatcher go offline
     */
    void goOffline();

    /**
     * response sync collect task data
     * @param jobId            jobId
     * @param metricsDataTemps collect data
     */
    void responseSyncJobData(String jobId, List<CollectMetricsParams> metricsDataTemps);
}
