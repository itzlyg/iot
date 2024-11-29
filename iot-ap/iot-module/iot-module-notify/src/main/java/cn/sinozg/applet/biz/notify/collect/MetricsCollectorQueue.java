package cn.sinozg.applet.biz.notify.collect;

import cn.sinozg.applet.biz.notify.timer.MetricsCollect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 19:17:18
 */
@Slf4j
@Component
public class MetricsCollectorQueue {

    private final PriorityBlockingQueue<MetricsCollect> jobQueue;

    public MetricsCollectorQueue() {
        jobQueue = new PriorityBlockingQueue<>();
    }

    public void addJob(MetricsCollect job) {
        jobQueue.offer(job);
    }

    public MetricsCollect getJob() throws InterruptedException {
        return jobQueue.poll(2, TimeUnit.SECONDS);
    }

}
