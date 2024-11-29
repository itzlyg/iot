package cn.sinozg.applet.biz.notify.dispatch;


import io.netty.util.Timeout;

/**
 * Metrics collection task scheduler interface
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 18:42:14
 */
public interface MetricsTaskDispatch {

    /**
     * schedule task
     * @param timeout timeout
     */
    void dispatchMetricsTask(Timeout timeout);
}
