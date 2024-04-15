package cn.sinozg.applet.turbo.engine.executor;

import cn.sinozg.applet.turbo.engine.bo.NodeInstanceBo;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import cn.sinozg.applet.turbo.tb.entity.NodeInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Description 
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-03-23 15:30:30
 */
public abstract class AbstractElementFactoryExecutor extends AbstractElementExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractElementFactoryExecutor.class);
    @Override
    protected AbstractRuntimeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        Map<String, FlowElement> flowElementMap = runtimeContext.getFlowElementMap();
        FlowElement flowElement = getUniqueNextNode(runtimeContext.getCurrentNodeModel(), flowElementMap);
        runtimeContext.setCurrentNodeModel(flowElement);
        return executorFactory.getElementExecutor(flowElement);
    }


    /**
     * Get elementExecutor to rollback:
     * Get sourceNodeInstanceId from currentNodeInstance and get sourceElement
     *
     * @return
     * @throws Exception
     */
    @Override
    protected AbstractElementExecutor getRollbackExecutor(RuntimeContext runtimeContext) throws ProcessException {
        String flowInstanceId = runtimeContext.getFlowInstanceId();
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();

        String sourceNodeInstanceId = currentNodeInstance.getSourceNodeInstanceId();
        if (StringUtils.isBlank(sourceNodeInstanceId)) {
            LOGGER.warn("getRollbackExecutor: there's no sourceNodeInstance(startEvent)."
                    + "||flowInstanceId={}||nodeInstanceId={}", flowInstanceId, currentNodeInstance.getNodeInstanceId());
            return null;
        }

        // TODO: 2019/12/13 get from cache
        NodeInstance sourceNodeInstance = nodeInstanceService.selectByNodeInstanceId(flowInstanceId, sourceNodeInstanceId);
        if (sourceNodeInstance == null) {
            LOGGER.warn("getRollbackExecutor failed: cannot find sourceNodeInstance from db."
                    + "||flowInstanceId={}||sourceNodeInstanceId={}", flowInstanceId, sourceNodeInstanceId);
            throw new ProcessException(ErrorEnum.GET_NODE_INSTANCE_FAILED);
        }

        FlowElement sourceNode = FlowModelUtil.getFlowElement(runtimeContext.getFlowElementMap(), sourceNodeInstance.getNodeKey());

        // TODO: 2019/12/18
        runtimeContext.setCurrentNodeModel(sourceNode);
        return executorFactory.getElementExecutor(sourceNode);
    }

}
