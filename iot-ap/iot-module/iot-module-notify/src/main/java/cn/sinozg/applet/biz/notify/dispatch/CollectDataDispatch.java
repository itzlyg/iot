package cn.sinozg.applet.biz.notify.dispatch;


import cn.sinozg.applet.biz.notify.model.collect.MetricsInfo;
import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;
import io.netty.util.Timeout;

import java.util.List;

/**
 * Collection data scheduler interface
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 18:42:03
 */
public interface CollectDataDispatch {

    /**
     * Processing and distributing collection result data
     * @param timeout     time wheel timeout        
     * @param metrics     The following metrics collection tasks   
     * @param metricsData Collect result data       
     */
    void dispatchCollectData(Timeout timeout, MetricsInfo metrics, CollectMetricsParams metricsData);

    /**
     * Processing and distributing collection result data
     *
     * @param timeout     time wheel timeout        
     * @param metrics     The following metrics collection tasks    
     * @param metricsDataList Collect result data       
     */
    void dispatchCollectData(Timeout timeout, MetricsInfo metrics, List<CollectMetricsParams> metricsDataList);

}
