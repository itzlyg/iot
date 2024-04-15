package cn.sinozg.applet.turbo.engine.executor;


import cn.sinozg.applet.common.utils.SnowFlake;
import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceMappingService;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceService;
import cn.sinozg.applet.turbo.tb.service.InstanceDataService;
import cn.sinozg.applet.turbo.tb.service.NodeInstanceLogService;
import cn.sinozg.applet.turbo.tb.service.NodeInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-03-23 15:30:30
 */
public abstract class AbstractRuntimeExecutor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractRuntimeExecutor.class);

    @Resource
    protected ExecutorFactory executorFactory;

    @Resource
    protected InstanceDataService instanceDataService;

    @Resource
    protected NodeInstanceService nodeInstanceService;

    @Resource
    protected FlowInstanceService flowInstanceService;

    @Resource
    protected NodeInstanceLogService nodeInstanceLogService;

    @Resource
    protected FlowInstanceMappingService flowInstanceMappingService;


    protected String genId() {
        return SnowFlake.genId();
    }

    public abstract void execute(RuntimeContext runtimeContext) throws ProcessException;

    public abstract void commit(RuntimeContext runtimeContext) throws ProcessException;

    public abstract void rollback(RuntimeContext runtimeContext) throws ProcessException;

    protected abstract boolean isCompleted(RuntimeContext runtimeContext) throws ProcessException;

    protected boolean isSubFlowInstance(RuntimeContext runtimeContext) throws ProcessException {
        return runtimeContext.getParentRuntimeContext() != null;
    }

    protected abstract AbstractRuntimeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException;

    protected abstract AbstractRuntimeExecutor getRollbackExecutor(RuntimeContext runtimeContext) throws ProcessException;
}
