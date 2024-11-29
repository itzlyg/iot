package cn.sinozg.applet.biz.notify.collect.impl;

import cn.sinozg.applet.biz.notify.collect.AbstractCollect;
import cn.sinozg.applet.biz.notify.model.collect.MetricsInfo;
import cn.sinozg.applet.biz.notify.model.proto.CollectMetricsParams;
import lombok.extern.slf4j.Slf4j;

/**
 * 收集器的实现
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-26 12:49:15
 */
@Slf4j
public class JdbcCommonCollect extends AbstractCollect {

    private static final String QUERY_TYPE_ONE_ROW = "oneRow";
    private static final String QUERY_TYPE_MULTI_ROW = "multiRow";
    private static final String QUERY_TYPE_COLUMNS = "columns";
    private static final String RUN_SCRIPT = "runScript";

    public JdbcCommonCollect(){}

    @Override
    public void collect(CollectMetricsParams builder, String monitorId, String app, MetricsInfo metrics) {

    }
}
