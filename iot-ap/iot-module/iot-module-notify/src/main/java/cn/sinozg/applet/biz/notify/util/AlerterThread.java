package cn.sinozg.applet.biz.notify.util;

import cn.sinozg.applet.common.core.ThreadFun;
import cn.sinozg.applet.common.handler.ThreadPoolExecutorHandler;
import cn.sinozg.applet.common.utils.ThreadPool;

import java.time.Duration;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 告警线程池
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-23 14:44
 */
public class AlerterThread {

    private static final ThreadPoolExecutor ALERTER_THREAD = new ThreadPoolExecutorHandler(ThreadFun.CORE_POOL_SIZE + 1, ThreadFun.CORE_POOL_SIZE + 1,
            10, TimeUnit.SECONDS, new SynchronousQueue<>(), "alerter-pool", new ThreadPoolExecutor.AbortPolicy());

    private static final ScheduledThreadPoolExecutor SCHEDULED_EXECUTOR = ThreadPool.scheduledThread("metrics-task-timeout-monitor");
    /**
     * 执行 告警线程任务
     * @param runnable 任务
     * @throws RejectedExecutionException 异常
     */
    public static void execute(Runnable runnable) throws RejectedExecutionException {
        ALERTER_THREAD.execute(runnable);
    }

    /**
     * 执行延迟任务
     * @param command 任务
     * @return 返回结果
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command){
        return ThreadPool.scheduleWithFixedDelay(SCHEDULED_EXECUTOR, command, Duration.ofSeconds(2), Duration.ofSeconds(20));
    }
}
