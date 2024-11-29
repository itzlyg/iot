package cn.sinozg.applet.biz.notify.collect;


import cn.sinozg.applet.biz.notify.model.collect.MetricsInfo;
import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;

/**
 * @Description 
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 18:41:50
 */
public abstract class AbstractCollect {

    /**
     * Real acquisition implementation interface
     * @param builder response builder
     * @param monitorId  monitor id   
     * @param app monitor type 
     * @param metrics metric configuration
     */
    public abstract void collect(CollectMetricsParams builder, String monitorId, String app, MetricsInfo metrics);

}
