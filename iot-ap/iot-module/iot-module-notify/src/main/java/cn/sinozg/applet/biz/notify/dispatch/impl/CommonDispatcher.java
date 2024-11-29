package cn.sinozg.applet.biz.notify.dispatch.impl;

import cn.sinozg.applet.biz.notify.collect.MetricsCollectorQueue;
import cn.sinozg.applet.biz.notify.collect.UnitConvert;
import cn.sinozg.applet.biz.notify.dispatch.CollectDataDispatch;
import cn.sinozg.applet.biz.notify.dispatch.MetricsTaskDispatch;
import cn.sinozg.applet.biz.notify.dispatch.TimerDispatch;
import cn.sinozg.applet.biz.notify.model.collect.Configmap;
import cn.sinozg.applet.biz.notify.model.collect.MetricsInfo;
import cn.sinozg.applet.biz.notify.model.collect.MetricsTime;
import cn.sinozg.applet.biz.notify.model.collect.TimerJobInfo;
import cn.sinozg.applet.biz.notify.model.proto.CollectCode;
import cn.sinozg.applet.biz.notify.model.proto.CollectFieldParams;
import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;
import cn.sinozg.applet.biz.notify.model.proto.CollectRowsParams;
import cn.sinozg.applet.biz.notify.service.AlerterDataQueueService;
import cn.sinozg.applet.biz.notify.timer.MetricsCollect;
import cn.sinozg.applet.biz.notify.timer.WheelTimerTask;
import cn.sinozg.applet.biz.notify.util.AlerterThread;
import cn.sinozg.applet.biz.notify.util.CollectUtil;
import cn.sinozg.applet.common.utils.JsonUtil;
import io.netty.util.Timeout;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 收集数据，满足条件的发送到消费队列
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 19:01:41
 */
@Slf4j
@Component
public class CommonDispatcher implements MetricsTaskDispatch, CollectDataDispatch {

    /**
     * Collection task timeout value
     */
    private static final long DURATION_TIME = 240_000L;
    /**
     * Trigger sub task max num
     */
    private static final int MAX_SUB_TASK_NUM = 50;
    /**
     * Collect Response env config length
     */
    private static final int ENV_CONFIG_SIZE = 1;
    /**
     * Priority queue of index collection tasks
     */
    private final MetricsCollectorQueue metricsCollectorQueue;
    /**
     * Time round task scheduler
     */
    private final TimerDispatch timerDispatch;
    /**
     * collection data exporter
     */
    private final AlerterDataQueueService dataQueueService;
    /**
     * Metrics task and start time mapping map
     */
    private final Map<String, MetricsTime> metricsTimeoutMonitorMap;

    private final List<UnitConvert> unitConvertList;
    
    private final String collectorIdentity;

    public CommonDispatcher(MetricsCollectorQueue metricsCollectorQueue,
                            TimerDispatch timerDispatch,
                            AlerterDataQueueService alerterDataQueueService,
                            List<UnitConvert> unitConvertList) {
        this.dataQueueService = alerterDataQueueService;
        this.metricsCollectorQueue = metricsCollectorQueue;
        this.timerDispatch = timerDispatch;
        this.unitConvertList = unitConvertList;
        this.collectorIdentity = "collectorIdentity";
        this.metricsTimeoutMonitorMap = new ConcurrentHashMap<>(16);
        this.start();
    }

    public void start() {
        try {
            // Pull the collection task from the task queue and put it into the thread pool for execution
            AlerterThread.execute(() -> {
                Thread.currentThread().setName("metrics-task-dispatcher");
                while (!Thread.currentThread().isInterrupted()) {
                    MetricsCollect metricsCollect = null;
                    try {
                        metricsCollect = metricsCollectorQueue.getJob();
                        if (metricsCollect != null) {
                            AlerterThread.execute(metricsCollect);
                        }
                    } catch (RejectedExecutionException rejected) {
                        log.info("[Dispatcher]-the worker pool is full, reject this metrics task，put in queue again.");
                        if (metricsCollect != null) {
                            metricsCollect.setRunPriority((byte) (metricsCollect.getRunPriority() + 1));
                            metricsCollectorQueue.addJob(metricsCollect);
                        }
                    } catch (InterruptedException interruptedException) {
                        log.info("[Dispatcher]-metrics-task-dispatcher has been interrupt to close.");
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        log.error("[Dispatcher]-{}.", e.getMessage(), e);
                    }
                }
                log.info("Thread Interrupted, Shutdown the [metrics-task-dispatcher]");
            });
            // monitoring metrics collection task execution timeout
            AlerterThread.scheduleWithFixedDelay(this::monitorCollectTaskTimeout);
        } catch (Exception e) {
            log.error("Common Dispatcher error: {}.", e.getMessage(), e);
        }
    }
    
    private void monitorCollectTaskTimeout() {
        try {
            // Detect whether the collection unit of each metrics has timed out for 4 minutes,
            // and if it times out, it will be discarded and an exception will be returned.
            long deadline = System.currentTimeMillis() - DURATION_TIME;
            for (Map.Entry<String, MetricsTime> entry : metricsTimeoutMonitorMap.entrySet()) {
                MetricsTime metricsTime = entry.getValue();
                if (metricsTime.getStartTime() < deadline) {
                    // Metrics collection timeout
                    WheelTimerTask timerJob = (WheelTimerTask) metricsTime.getTimeout().task();
                    TimerJobInfo jobInfo = timerJob.getJob();
                    MetricsInfo metricsInfo = metricsTime.getMetrics();
                    CollectMetricsParams metricsData = CollectMetricsParams.builder()
                            .id(jobInfo.getId())
                            .tenantId(jobInfo.getTenantId())
                            .app(jobInfo.getApp())
                            .metrics(metricsInfo.getName())
                            .priority(metricsInfo.getPriority())
                            .time(System.currentTimeMillis())
                            .code(CollectCode.TIMEOUT)
                            .msg("collect timeout")
                            .build();
                    log.error("[Collect Timeout]: \n{}", metricsData);
                    if (metricsData.getPriority() == 0) {
                        dispatchCollectData(metricsTime.getTimeout(), metricsTime.getMetrics(), metricsData);
                    }
                    metricsTimeoutMonitorMap.remove(entry.getKey());
                }
            }
        } catch (Exception e) {
            log.error("[Task Timeout Monitor]-{}.", e.getMessage(), e);
        }
    }

    @Override
    public void dispatchMetricsTask(Timeout timeout) {
        // Divide the collection task of a single application into corresponding collection tasks of the metrics according to the metrics under it.
        // Put each collect task into the thread pool for scheduling
        WheelTimerTask timerTask = (WheelTimerTask) timeout.task();
        TimerJobInfo job = timerTask.getJob();
        job.constructPriorMetrics();
        Set<MetricsInfo> metricsSet = job.getNextCollectMetrics(null, true);
        metricsSet.forEach(metrics -> {
            MetricsCollect metricsCollect = new MetricsCollect(metrics, timeout, this, collectorIdentity, unitConvertList);
            metricsCollectorQueue.addJob(metricsCollect);
            MetricsTime time = toMetricsTime(timeout, metrics);
            metricsTimeoutMonitorMap.put(job.getId() + "-" + metrics.getName(), time);
        });
    }

    @Override
    public void dispatchCollectData(Timeout timeout, MetricsInfo metrics, CollectMetricsParams metricsData) {
        WheelTimerTask timerJob = (WheelTimerTask) timeout.task();
        TimerJobInfo job = timerJob.getJob();
        if (metrics.isHasSubTask()) {
            metricsTimeoutMonitorMap.remove(job.getId() + "-" + metrics.getName() + "-sub-" + metrics.getSubTaskId());
            boolean isLastTask = metrics.consumeSubTaskResponse(metricsData);
            if (isLastTask) {
                metricsData = metrics.getSubTaskDataRef().get();
            } else {
                return;
            }
        } else {
            metricsTimeoutMonitorMap.remove(job.getId() + "-" + metrics.getName());
        }
        Set<MetricsInfo> metricsSet = job.getNextCollectMetrics(metrics, false);
        if (job.isCyclic()) {
            // If it is an asynchronous periodic cyclic task, directly response the collected data
            dataQueueService.sendMetricsData(metricsData);
            if (log.isDebugEnabled()) {
                log.debug("Cyclic Job: {} - {} - {}", job.getMonitorId(), job.getApp(), metricsData.getMetrics());
                for (CollectRowsParams valueRow : metricsData.getValue()) {
                    for (CollectFieldParams field : metricsData.getFields()) {
                        int index = metricsData.getFields().indexOf(field);
                        log.debug("Field-->{},Value-->{}", field.getName(), valueRow.getColumns().get(index));
                    }
                }
            }
            // If metricsSet is null, it means that the execution is completed or whether the priority of the collection metrics is 0, that is, the availability collection metrics.
            // If the availability collection fails, the next metrics scheduling will be cancelled and the next round of scheduling will be entered directly.
            boolean isAvailableCollectFailed = metricsSet != null && !metricsSet.isEmpty()
                    && metrics.getPriority() == (byte) 0 && metricsData.getCode() != CollectCode.SUCCESS;
            if (metricsSet == null || isAvailableCollectFailed) {
                // The collection and execution task of this job are completed.
                // The periodic task pushes the task to the time wheel again.
                // First, determine the execution time of the task and the task collection interval.
                if (timeout.isCancelled()) {
                    return;
                }
                long spendTime = System.currentTimeMillis() - job.getDispatchTime();
                long interval = job.getInterval() - spendTime / 1000;
                interval = Math.max(interval, 0);
                // Reset Construction Execution Metrics Task View 
                job.constructPriorMetrics();
                timerDispatch.cyclicJob(timerJob, interval, TimeUnit.SECONDS);
            } else if (!metricsSet.isEmpty()) {
                // The execution of the current level metrics is completed, and the execution of the next level metrics starts
                // use pre collect metrics data to replace next metrics config params
                List<Map<String, Configmap>> configmapList = getConfigmapFromPreCollectData(metricsData);
                if (configmapList.size() == ENV_CONFIG_SIZE) {
                    job.addEnvConfigmaps(configmapList.get(0));
                }
                for (MetricsInfo metricItem : metricsSet) {
                    Set<String> cryPlaceholderFields = CollectUtil.matchCryPlaceholderField(JsonUtil.toNode(metricItem));
                    if (cryPlaceholderFields.isEmpty()) {
                        MetricsCollect metricsCollect = new MetricsCollect(metricItem, timeout, this, collectorIdentity, unitConvertList);
                        metricsCollectorQueue.addJob(metricsCollect);
                        MetricsTime time = toMetricsTime(timeout, metrics);
                        metricsTimeoutMonitorMap.put(job.getId() + "-" + metricItem.getName(), time);
                        continue;
                    }
                    boolean isSubTask = configmapList.stream().anyMatch(map -> map.keySet().stream().anyMatch(cryPlaceholderFields::contains));
                    int subTaskNum = isSubTask ? Math.min(configmapList.size(), MAX_SUB_TASK_NUM) : 1;
                    AtomicInteger subTaskNumAtomic = new AtomicInteger(subTaskNum);
                    AtomicReference<CollectMetricsParams> metricsDataReference = new AtomicReference<>();
                    for (int index = 0; index < subTaskNum; index++) {
                        Map<String, Configmap> configmap = new HashMap<>(job.getEnvConfigmaps());
                        if (isSubTask) {
                            Map<String, Configmap> preConfigMap = configmapList.get(index);
                            configmap.putAll(preConfigMap);
                        }
                        MetricsInfo metric = CollectUtil.replaceCryPlaceholder(metricItem, configmap);
                        metric.setSubTaskNum(subTaskNumAtomic);
                        metric.setSubTaskId(index);
                        metric.setSubTaskDataRef(metricsDataReference);
                        MetricsCollect metricsCollect = new MetricsCollect(metric, timeout, this, collectorIdentity, unitConvertList);
                        metricsCollectorQueue.addJob(metricsCollect);
                        MetricsTime time = toMetricsTime(timeout, metrics);
                        metricsTimeoutMonitorMap.put(job.getId() + "-" + metric.getName() + "-sub-" + index, time);
                    }

                }
            } else {
                // The list of metrics at the current execution level has not been fully executed.
                // It needs to wait for the execution of other metrics task of the same level to complete the execution and enter the next level for execution.
            }
        } else {
            // If it is a temporary one-time task, you need to wait for the collected data of all metrics task to be packaged and returned.
            // Insert the current metrics data into the job for unified assembly
            job.addCollectMetricsData(metricsData);
            if (log.isDebugEnabled()) {
                if (log.isDebugEnabled()) {
                    log.debug("One-time Job: {}", metricsData.getMetrics());
                }
                for (CollectRowsParams valueRow : metricsData.getValue()) {
                    for (CollectFieldParams field : metricsData.getFields()) {
                        int index = metricsData.getFields().indexOf(field);
                        if (log.isDebugEnabled()) {
                            log.debug("Field-->{}, Value-->{}", field.getName(), valueRow.getColumns().get(index));
                        }
                    }
                }
            }
            if (metricsSet == null) {
                // The collection and execution of all metrics of this job are completed
                // and the result listener is notified of the combination of all metrics data
                timerDispatch.responseSyncJobData(job.getId(), job.getResponseDataTemp());
            } else if (!metricsSet.isEmpty()) {
                // The execution of the current level metrics is completed, and the execution of the next level metrics starts
                metricsSet.forEach(metricItem -> {
                    MetricsCollect metricsCollect = new MetricsCollect(metricItem, timeout, this, collectorIdentity, unitConvertList);
                    metricsCollectorQueue.addJob(metricsCollect);
                    MetricsTime time = toMetricsTime(timeout, metrics);
                    metricsTimeoutMonitorMap.put(job.getId() + "-" + metricItem.getName(), time);
                });
            } else {
                // The list of metrics task at the current execution level has not been fully executed.
                // It needs to wait for the execution of other metrics task of the same level to complete the execution and enter the next level for execution.
            }
        }
    }

    @Override
    public void dispatchCollectData(Timeout timeout, MetricsInfo metrics, List<CollectMetricsParams> metricsDataList) {
        WheelTimerTask timerJob = (WheelTimerTask) timeout.task();
        TimerJobInfo job = timerJob.getJob();
        metricsTimeoutMonitorMap.remove(job.getId());
        if (job.isCyclic()) {
            // If it is an asynchronous periodic cyclic task, directly response the collected data
            metricsDataList.forEach(dataQueueService::sendMetricsData);
            // The collection and execution of all task of this job are completed.
            // The periodic task pushes the task to the time wheel again.
            // First, determine the execution time of the task and the task collection interval.
            if (timeout.isCancelled()) {
                return;
            }
            long spendTime = System.currentTimeMillis() - job.getDispatchTime();
            long interval = job.getInterval() - spendTime / 1000;
            interval = Math.max(interval, 0);
            // Reset Construction Execution Metrics Task View 
            job.constructPriorMetrics();
            timerDispatch.cyclicJob(timerJob, interval, TimeUnit.SECONDS);
        } else {
            // The collection and execution of all metrics of this job are completed
            // and the result listener is notified of the combination of all metrics data
            timerDispatch.responseSyncJobData(job.getId(), metricsDataList);
        }
        
    }

    private List<Map<String, Configmap>> getConfigmapFromPreCollectData(CollectMetricsParams metricsData) {
        if (CollectionUtils.isEmpty(metricsData.getValue()) || CollectionUtils.isEmpty(metricsData.getFields())) {
            return new LinkedList<>();
        }
        List<Map<String, Configmap>> mapList = new LinkedList<>();
        for (CollectRowsParams valueRow : metricsData.getValue()) {
            if (valueRow.getColumnsCount() != metricsData.getFieldsCount()) {
                continue;
            }
            Map<String, Configmap> configmapMap = new HashMap<>(valueRow.getColumnsCount());
            int index = 0;
            for (CollectFieldParams field : metricsData.getFields()) {
                String value = valueRow.getColumns().get(index);
                index++;
                Configmap configmap = new Configmap();
                configmap.setKey(field.getName());
                configmap.setValue(value);
                configmap.setType(Integer.valueOf(field.getType()).byteValue());
                configmapMap.put(field.getName(), configmap);
            }
            mapList.add(configmapMap);
        }
        return mapList;
    }

    private MetricsTime toMetricsTime(Timeout timeout, MetricsInfo metrics){
        MetricsTime time = new MetricsTime();
        time.setTimeout(timeout);
        time.setMetrics(metrics);
        time.setStartTime(System.currentTimeMillis());
        return time;
    }
}
