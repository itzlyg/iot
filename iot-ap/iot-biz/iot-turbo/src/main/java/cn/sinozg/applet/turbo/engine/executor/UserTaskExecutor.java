package cn.sinozg.applet.turbo.engine.executor;

import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.turbo.engine.bo.NodeInstanceBo;
import cn.sinozg.applet.turbo.engine.common.Constants;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.common.NodeInstanceStatus;
import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.exception.SuspendException;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

@Service(value = FlowElementType.NM_USER_TASK)
public class UserTaskExecutor extends AbstractElementExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskExecutor.class);

    @Override
    protected void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        if (currentNodeInstance.getStatus() == NodeInstanceStatus.COMPLETED) {
            LOGGER.warn("doExecute reentrant: currentNodeInstance is completed.||runtimeContext={}", runtimeContext);
            return;
        }

        if (currentNodeInstance.getStatus() != NodeInstanceStatus.ACTIVE) {
            currentNodeInstance.setStatus(NodeInstanceStatus.ACTIVE);
        }
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);

        FlowElement flowElement = runtimeContext.getCurrentNodeModel();
        String nodeName = FlowModelUtil.getElementName(flowElement);
        LOGGER.info("doExecute: userTask to commit.||flowInstanceId={}||nodeInstanceId={}||nodeKey={}||nodeName={}",
                runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId(), flowElement.getKey(), nodeName);
        throw new SuspendException(ErrorEnum.COMMIT_SUSPEND, MessageFormat.format(Constants.NODE_INSTANCE_FORMAT,
                flowElement.getKey(), nodeName, currentNodeInstance.getNodeInstanceId()));
    }

    @Override
    protected void preCommit(RuntimeContext runtimeContext) throws ProcessException {
        String flowInstanceId = runtimeContext.getFlowInstanceId();
        NodeInstanceBo suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        String nodeInstanceId = suspendNodeInstance.getNodeInstanceId();
        int status = suspendNodeInstance.getStatus();
        FlowElement flowElement = runtimeContext.getCurrentNodeModel();
        String nodeName = FlowModelUtil.getElementName(flowElement);
        String nodeKey = flowElement.getKey();

        NodeInstanceBo currentNodeInstance = PojoUtil.copyBean(suspendNodeInstance, NodeInstanceBo.class);
        runtimeContext.setCurrentNodeInstance(currentNodeInstance);

        //invalid commit node
        if (!suspendNodeInstance.getNodeKey().equals(nodeKey)) {
            LOGGER.warn("preCommit: invalid nodeKey to commit.||flowInstanceId={}||nodeInstanceId={}||nodeKey={}||nodeName={}",
                    flowInstanceId, nodeInstanceId, nodeKey, nodeName);
            throw new ProcessException(ErrorEnum.COMMIT_FAILED, MessageFormat.format(Constants.NODE_INSTANCE_FORMAT,
                    flowElement.getKey(), nodeName, currentNodeInstance.getNodeInstanceId()));
        }

        //reentrant: completed
        if (status == NodeInstanceStatus.COMPLETED) {
            LOGGER.warn("preCommit: userTask is completed.||flowInstanceId={}||nodeInstanceId={}||nodeKey={}",
                    flowInstanceId, nodeInstanceId, nodeKey);
            return;
        }

        //invalid status
        if (status != NodeInstanceStatus.ACTIVE) {
            LOGGER.warn("preCommit: invalid status to commit.||flowInstanceId={}||status={}||nodeInstanceId={}||nodeKey={}",
                    flowInstanceId, status, nodeInstanceId, nodeKey);
            throw new ProcessException(ErrorEnum.COMMIT_FAILED, MessageFormat.format(Constants.NODE_INSTANCE_FORMAT,
                    flowElement.getKey(), nodeName, currentNodeInstance.getNodeInstanceId()));
        }
    }

    @Override
    protected void postCommit(RuntimeContext runtimeContext) {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        if (currentNodeInstance.getStatus() != NodeInstanceStatus.COMPLETED) {
            currentNodeInstance.setStatus(NodeInstanceStatus.COMPLETED);
            runtimeContext.getNodeInstanceList().add(currentNodeInstance);
        }
    }

    /**
     * Rollback: turn status to DISABLED.
     * If nodeInstance.status is COMPLETED, create new nodeInstance as currentNodeInstance.
     * <p>
     * SuspendException: while need suspend and status is COMPLETED
     */
    @Override
    protected void doRollback(RuntimeContext runtimeContext) throws ProcessException {

        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        int currentStatus = currentNodeInstance.getStatus();
        currentNodeInstance.setStatus(NodeInstanceStatus.DISABLED);
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
        if (currentStatus == NodeInstanceStatus.COMPLETED) {
            NodeInstanceBo newNodeInstance = PojoUtil.copyBean(currentNodeInstance, NodeInstanceBo.class);
            // TODO: 2019/12/31 to insert new record
            newNodeInstance.setId(null);
            String newNodeInstanceId = genId();
            newNodeInstance.setNodeInstanceId(newNodeInstanceId);
            newNodeInstance.setStatus(NodeInstanceStatus.ACTIVE);
            runtimeContext.setCurrentNodeInstance(newNodeInstance);
            runtimeContext.getNodeInstanceList().add(newNodeInstance);
            throw new SuspendException(ErrorEnum.ROLLBACK_SUSPEND, MessageFormat.format(Constants.NODE_INSTANCE_FORMAT,
                    newNodeInstance.getNodeKey(),
                    FlowModelUtil.getFlowElement(runtimeContext.getFlowElementMap(), newNodeInstance.getNodeKey()),
                    currentNodeInstance.getNodeInstanceId()));
        }
        LOGGER.info("doRollback.||currentNodeInstance={}||nodeKey={}", currentNodeInstance, currentNodeInstance.getNodeKey());
    }

    @Override
    protected void postRollback(RuntimeContext runtimeContext) throws ProcessException {
        //do nothing
    }


    /**
     * Calculate unique outgoing
     * Case1.unique outgoing;
     * Case2.calculateOne from multiple outgoings as exclusiveGateway
     * Calculate expression: one of flowElement's properties
     * Calculate input: data map
     *
     * @return
     * @throws Exception
     */
    @Override
    protected AbstractRuntimeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        FlowElement currentFlowElement = runtimeContext.getCurrentNodeModel();
        Map<String, FlowElement> flowElementMap = runtimeContext.getFlowElementMap();

        FlowElement nextNode;
        if (PojoUtil.single(currentFlowElement.getOutgoing())) {
            //case1. unique outgoing
            nextNode = getUniqueNextNode(currentFlowElement, flowElementMap);
        } else {
            //case2. multiple outgoings and calculate the next node with instanceDataMap
            nextNode = calculateNextNode(currentFlowElement, flowElementMap, runtimeContext.getInstanceDataMap());
        }
        LOGGER.info("getExecuteExecutor.||nextNode={}||runtimeContext={}", nextNode, runtimeContext);

        runtimeContext.setCurrentNodeModel(nextNode);

        return executorFactory.getElementExecutor(nextNode);
    }
}
