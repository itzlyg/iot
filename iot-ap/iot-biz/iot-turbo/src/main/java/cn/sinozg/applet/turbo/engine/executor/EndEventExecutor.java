package cn.sinozg.applet.turbo.engine.executor;

import cn.sinozg.applet.turbo.engine.bo.NodeInstanceBo;
import cn.sinozg.applet.turbo.engine.common.Constants;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.common.NodeInstanceStatus;
import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service(value = FlowElementType.NM_END_EVENT)
public class EndEventExecutor extends AbstractElementExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndEventExecutor.class);

    @Override
    protected void postExecute(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        currentNodeInstance.setInstanceDataId(runtimeContext.getInstanceDataId());
        currentNodeInstance.setStatus(NodeInstanceStatus.COMPLETED);
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
    }

    @Override
    protected void doRollback(RuntimeContext runtimeContext) throws ProcessException {
        // when subFlowInstance, the EndEvent rollback is allowed
        if (isSubFlowInstance(runtimeContext)) {
            return;
        }
        FlowElement flowElement = runtimeContext.getCurrentNodeModel();
        String nodeName = FlowModelUtil.getElementName(flowElement);
        LOGGER.warn("doRollback: unsupported element type as EndEvent.||flowInstanceId={}||nodeKey={}||nodeName={}||nodeType={}",
            runtimeContext.getFlowInstanceId(), flowElement.getKey(), nodeName, flowElement.getType());
        throw new ProcessException(ErrorEnum.UNSUPPORTED_ELEMENT_TYPE,
            MessageFormat.format(Constants.NODE_INFO_FORMAT, flowElement.getKey(), nodeName, flowElement.getType()));
    }

    @Override
    protected void postRollback(RuntimeContext runtimeContext) throws ProcessException {
        // when subFlowInstance, the EndEvent rollback is allowed
        if (isSubFlowInstance(runtimeContext)) {
            super.postRollback(runtimeContext);
            return;
        }
        //do nothing
    }

    @Override
    protected AbstractRuntimeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        LOGGER.info("getExecuteExecutor: no executor after EndEvent.");
        return null;
    }
}
