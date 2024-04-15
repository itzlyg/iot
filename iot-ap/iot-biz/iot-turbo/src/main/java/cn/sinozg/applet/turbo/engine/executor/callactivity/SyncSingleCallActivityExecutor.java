package cn.sinozg.applet.turbo.engine.executor.callactivity;

import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.turbo.engine.bo.NodeInstanceBo;
import cn.sinozg.applet.turbo.engine.bo.NodeInstanceMidBo;
import cn.sinozg.applet.turbo.engine.common.Constants;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.common.FlowInstanceMappingType;
import cn.sinozg.applet.turbo.engine.common.FlowInstanceStatus;
import cn.sinozg.applet.turbo.engine.common.NodeInstanceStatus;
import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.exception.SuspendException;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;
import cn.sinozg.applet.turbo.engine.param.CommitTaskParam;
import cn.sinozg.applet.turbo.engine.param.RollbackTaskParam;
import cn.sinozg.applet.turbo.engine.param.StartProcessParam;
import cn.sinozg.applet.turbo.engine.result.CommitTaskResult;
import cn.sinozg.applet.turbo.engine.result.RollbackTaskResult;
import cn.sinozg.applet.turbo.engine.result.RuntimeResult;
import cn.sinozg.applet.turbo.engine.result.StartProcessResult;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import cn.sinozg.applet.turbo.engine.util.InstanceDataUtil;
import cn.sinozg.applet.turbo.tb.entity.FlowDeployment;
import cn.sinozg.applet.turbo.tb.entity.FlowInstance;
import cn.sinozg.applet.turbo.tb.entity.FlowInstanceMapping;
import cn.sinozg.applet.turbo.tb.entity.InstanceData;
import cn.sinozg.applet.turbo.tb.entity.NodeInstance;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * CallActivityExecutor base on sync and single instance mode,
 * support the dynamic assignment of subFlowModule on the execution side
 * <p>
 * feature e.g.
 * 1.Automatically suspend when executing to CallActivity node
 * 2.External systems can attach unique attributes on CallActivity node
 * 3.When External systems compute subFlowModuleId success, need continue to submit downward
 * 4.CallActivity node support repeated submission
 */
@Service(value = FlowElementType.NM_CALL_ACTIVITY)
public class SyncSingleCallActivityExecutor extends AbstractCallActivityExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncSingleCallActivityExecutor.class);

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
        LOGGER.info("doExecute: syncSingleCallActivity to commit.||flowInstanceId={}||nodeInstanceId={}||nodeKey={}||nodeName={}",
            runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId(), flowElement.getKey(), nodeName);
        throw new SuspendException(ErrorEnum.COMMIT_SUSPEND, MessageFormat.format(Constants.NODE_INSTANCE_FORMAT,
            flowElement.getKey(), nodeName, currentNodeInstance.getNodeInstanceId()));
    }

    @Override
    protected void preCommit(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBo suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        NodeInstanceBo currentNodeInstance = PojoUtil.copyBean(suspendNodeInstance, NodeInstanceBo.class);
        runtimeContext.setCurrentNodeInstance(currentNodeInstance);
    }

    @Override
    protected void doCommit(RuntimeContext runtimeContext) throws ProcessException {
        boolean commitCallActivityNode = CollectionUtils.isEmpty(runtimeContext.getSuspendNodeInstanceStack());
        if (commitCallActivityNode) {
            startProcessCallActivity(runtimeContext);
        } else {
            commitCallActivity(runtimeContext);
        }
    }

    @Override
    protected void postCommit(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
    }

    @Override
    protected void doRollback(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        FlowInstanceMapping flowInstanceMapping = flowInstanceMappingService.selectFlowInstanceMapping(runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId());
        String subFlowInstanceId = flowInstanceMapping.getSubFlowInstanceId();

        String taskInstanceId;
        if (CollectionUtils.isEmpty(runtimeContext.getSuspendNodeInstanceStack())) {
            NodeInstance nodeInstance = nodeInstanceDataService.selectRecentEndNode(subFlowInstanceId);
            taskInstanceId = nodeInstance.getNodeInstanceId();
        } else {
            taskInstanceId = runtimeContext.getSuspendNodeInstanceStack().pop();
        }

        RollbackTaskParam rollbackTaskParam = new RollbackTaskParam();
        rollbackTaskParam.setRuntimeContext(runtimeContext);
        rollbackTaskParam.setFlowInstanceId(subFlowInstanceId);
        rollbackTaskParam.setTaskInstanceId(taskInstanceId);
        RollbackTaskResult rollbackTaskResult = runtimeProcessor.rollback(rollbackTaskParam);
        LOGGER.info("callActivity rollback.||rollbackTaskParam={}||rollbackTaskResult={}", rollbackTaskParam, rollbackTaskResult);
        // 4.update flowInstance mapping
        updateFlowInstanceMapping(runtimeContext);
        handleCallActivityResult(runtimeContext, rollbackTaskResult);
    }

    @Override
    protected void postRollback(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
    }

    protected void startProcessCallActivity(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        // 1.check reentrant execute
        FlowInstanceMapping flowInstanceMapping = flowInstanceMappingService.selectFlowInstanceMapping(runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId());
        if (flowInstanceMapping != null) {
            handleReentrantSubFlowInstance(runtimeContext, flowInstanceMapping);
            return;
        }
        // 2.check CallActivity nested level
        preCheckCallActivityNestedLevel(runtimeContext);

        // 3.get flowModuleId
        String callActivityFlowModuleId = runtimeContext.getCallActivityFlowModuleId();
        runtimeContext.setCallActivityFlowModuleId(null); // avoid misuse
        // 4.calculate variables
        List<InstanceDataVo> callActivityVariables = getCallActivityVariables(runtimeContext);

        StartProcessParam startProcessParam = new StartProcessParam();
        startProcessParam.setRuntimeContext(runtimeContext);
        startProcessParam.setFlowModuleId(callActivityFlowModuleId);
        startProcessParam.setVariables(callActivityVariables);
        StartProcessResult startProcessResult = runtimeProcessor.startProcess(startProcessParam);
        LOGGER.info("callActivity startProcess.||startProcessParam={}||startProcessResult={}", startProcessParam, startProcessResult);
        // 5.save flowInstance mapping
        saveFlowInstanceMapping(runtimeContext, startProcessResult.getFlowInstanceId());
        handleCallActivityResult(runtimeContext, startProcessResult);
    }

    private void preCheckCallActivityNestedLevel(RuntimeContext runtimeContext) throws ProcessException {
        int maxCallActivityNestedLevel = businessConfig.getCallActivityNestedLevel(runtimeContext.getCaller());
        int currentCallActivityNestedLevel = 0;
        RuntimeContext tmpRuntimeContext = runtimeContext;
        while (tmpRuntimeContext != null) {
            currentCallActivityNestedLevel++;
            tmpRuntimeContext = tmpRuntimeContext.getParentRuntimeContext();
        }
        if (maxCallActivityNestedLevel < currentCallActivityNestedLevel) {
            throw new ProcessException(ErrorEnum.FLOW_NESTED_LEVEL_EXCEEDED);
        }
    }

    private void saveFlowInstanceMapping(RuntimeContext runtimeContext, String subFlowInstanceId) {
        FlowInstanceMapping flowInstanceMapping = new FlowInstanceMapping();
        flowInstanceMapping.setFlowInstanceId(runtimeContext.getFlowInstanceId());
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        flowInstanceMapping.setNodeKey(currentNodeInstance.getNodeKey());
        flowInstanceMapping.setNodeInstanceId(currentNodeInstance.getNodeInstanceId());
        flowInstanceMapping.setSubFlowInstanceId(subFlowInstanceId);
        flowInstanceMapping.setType(FlowInstanceMappingType.EXECUTE);
        flowInstanceMapping.setTenant(runtimeContext.getTenant());
        flowInstanceMapping.setCaller(runtimeContext.getCaller());
        flowInstanceMappingService.save(flowInstanceMapping);
    }

    private void handleReentrantSubFlowInstance(RuntimeContext runtimeContext, FlowInstanceMapping flowInstanceMapping) throws ProcessException {
        String subFlowInstanceId = flowInstanceMapping.getSubFlowInstanceId();
        RuntimeResult subFlowInstanceFirstUserTask = getSubFlowInstanceFirstUserTask(subFlowInstanceId);
        if (subFlowInstanceFirstUserTask != null) {
            runtimeContext.setCallActivityRuntimeResultList(Collections.singletonList(subFlowInstanceFirstUserTask));
            throw new SuspendException(ErrorEnum.COMMIT_SUSPEND);
        }
        LOGGER.info("callActivity did not find userTask.||subFlowInstanceId={}", subFlowInstanceId);
    }

    private RuntimeResult getSubFlowInstanceFirstUserTask(String subFlowInstanceId) {
        FlowInstance subFlowInstance = flowInstanceService.selectByFlowInstanceId(subFlowInstanceId);
        FlowDeployment subFlowDeployment = flowDeploymentService.selectByDeployId(subFlowInstance.getFlowDeployId());
        Map<String, FlowElement> subFlowElementMap = FlowModelUtil.getFlowElementMap(subFlowDeployment.getFlowModel());

        List<NodeInstance> nodeInstanceList = nodeInstanceService.selectByFlowInstanceId(subFlowInstanceId);
        for (NodeInstance nodeInstance : nodeInstanceList) {
            int elementType = FlowModelUtil.getElementType(nodeInstance.getNodeKey(), subFlowElementMap);
            if (elementType == FlowElementType.USER_TASK) {
                return buildCallActivityFirstUserTaskRuntimeResult(subFlowInstance, subFlowElementMap, nodeInstance);
            } else if (elementType == FlowElementType.CALL_ACTIVITY) {
                FlowInstanceMapping flowInstanceMapping = flowInstanceMappingService.selectFlowInstanceMapping(subFlowInstanceId, nodeInstance.getNodeInstanceId());
                if (flowInstanceMapping == null) {
                    LOGGER.warn("callActivity did not find instanceMapping.||subFlowInstanceId={}", subFlowInstanceId);
                    break;
                }
                RuntimeResult runtimeResult = getSubFlowInstanceFirstUserTask(flowInstanceMapping.getSubFlowInstanceId());
                if (runtimeResult != null) {
                    return runtimeResult;
                }
            }
        }
        return null;
    }

    private RuntimeResult buildCallActivityFirstUserTaskRuntimeResult(FlowInstance subFlowInstance, Map<String, FlowElement> subFlowElementMap, NodeInstance nodeInstance) {
        RuntimeResult runtimeResult = new RuntimeResult();
        runtimeResult.setErrCode(ErrorEnum.COMMIT_SUSPEND.getErrNo());
        runtimeResult.setErrMsg(ErrorEnum.COMMIT_SUSPEND.getErrMsg());
        runtimeResult.setFlowInstanceId(subFlowInstance.getFlowInstanceId());
        runtimeResult.setStatus(subFlowInstance.getStatus());

        NodeInstanceMidBo nodeInstanceMid = PojoUtil.copyBean(nodeInstance, NodeInstanceMidBo.class);
        nodeInstanceMid.setCreateTime(null);
        nodeInstanceMid.setModifyTime(null);
        nodeInstanceMid.setModelKey(nodeInstance.getNodeKey());
        FlowElement flowElement = subFlowElementMap.get(nodeInstance.getNodeKey());
        nodeInstanceMid.setModelName(FlowModelUtil.getElementName(flowElement));
        nodeInstanceMid.setProperties(flowElement.getProperties());

        runtimeResult.setActiveTaskInstance(nodeInstanceMid);
        InstanceData instanceData = instanceDataService.selectByIns(subFlowInstance.getFlowInstanceId(), nodeInstance.getInstanceDataId());
        Map<String, InstanceDataVo> instanceDataMap = InstanceDataUtil.getInstanceDataMap(instanceData.getInstanceData());
        runtimeResult.setVariables(InstanceDataUtil.getInstanceDataList(instanceDataMap));
        return runtimeResult;
    }

    protected void commitCallActivity(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBo suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        FlowInstanceMapping flowInstanceMapping = flowInstanceMappingService.selectFlowInstanceMapping(runtimeContext.getFlowInstanceId(), suspendNodeInstance.getNodeInstanceId());
        String subFlowInstanceId = flowInstanceMapping.getSubFlowInstanceId();

        CommitTaskParam commitTaskParam = new CommitTaskParam();
        commitTaskParam.setRuntimeContext(runtimeContext);
        commitTaskParam.setFlowInstanceId(subFlowInstanceId);
        commitTaskParam.setTaskInstanceId(runtimeContext.getSuspendNodeInstanceStack().pop());
        commitTaskParam.setVariables(InstanceDataUtil.getInstanceDataList(runtimeContext.getInstanceDataMap()));
        // transparent transmission callActivity param
        commitTaskParam.setCallActivityFlowModuleId(runtimeContext.getCallActivityFlowModuleId());
        runtimeContext.setCallActivityFlowModuleId(null); // avoid misuse

        CommitTaskResult commitTaskResult = runtimeProcessor.commit(commitTaskParam);
        LOGGER.info("callActivity commit.||commitTaskParam={}||commitTaskResult={}", commitTaskParam, commitTaskResult);
        handleCallActivityResult(runtimeContext, commitTaskResult);
    }

    private void updateFlowInstanceMapping(RuntimeContext runtimeContext) {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        if (currentNodeInstance.getStatus() != NodeInstanceStatus.COMPLETED) {
            return;
        }
        currentNodeInstance.setStatus(NodeInstanceStatus.DISABLED);
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);

        NodeInstanceBo newNodeInstance = PojoUtil.copyBean(currentNodeInstance, NodeInstanceBo.class);
        newNodeInstance.setId(null);
        String newNodeInstanceId = genId();
        newNodeInstance.setNodeInstanceId(newNodeInstanceId);
        newNodeInstance.setStatus(NodeInstanceStatus.ACTIVE);
        runtimeContext.setCurrentNodeInstance(newNodeInstance);

        FlowInstanceMapping oldFlowInstanceMapping = flowInstanceMappingService.selectFlowInstanceMapping(runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId());
        flowInstanceMappingService.updateType(oldFlowInstanceMapping.getFlowInstanceId(), oldFlowInstanceMapping.getNodeInstanceId(), FlowInstanceMappingType.TERMINATED);

        FlowInstanceMapping newFlowInstanceMapping = PojoUtil.copyBean(oldFlowInstanceMapping, FlowInstanceMapping.class);
        newFlowInstanceMapping.setId(null);
        newFlowInstanceMapping.setNodeInstanceId(newNodeInstanceId);
        flowInstanceMappingService.save(newFlowInstanceMapping);
    }

    /**
     * common handle RuntimeResult from startProcessCallActivity, commitCallActivity, rollbackCallActivity.
     *
     * @param runtimeContext
     * @param runtimeResult
     * @throws ProcessException
     */
    protected void handleCallActivityResult(RuntimeContext runtimeContext, RuntimeResult runtimeResult) throws ProcessException {
        ErrorEnum errorEnum = ErrorEnum.getErrorEnum(runtimeResult.getErrCode());
        switch (Objects.requireNonNull(errorEnum)) {
            case SUCCESS:
                handleSuccessSubFlowResult(runtimeContext, runtimeResult);
                break;
            case COMMIT_SUSPEND:
            case ROLLBACK_SUSPEND:
                runtimeContext.getCurrentNodeInstance().setStatus(NodeInstanceStatus.ACTIVE);
                runtimeContext.setCallActivityRuntimeResultList(Collections.singletonList(runtimeResult));
                throw new SuspendException(errorEnum);
            default:
                throw new ProcessException(errorEnum);
        }
    }

    private void handleSuccessSubFlowResult(RuntimeContext runtimeContext, RuntimeResult runtimeResult) throws ProcessException {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        if (runtimeResult.getStatus() == FlowInstanceStatus.TERMINATED) {
            // The subFlow rollback from the StartNode to the MainFlow
            currentNodeInstance.setStatus(NodeInstanceStatus.DISABLED);
            flowInstanceMappingService.updateType(runtimeContext.getFlowInstanceId(), currentNodeInstance.getNodeInstanceId(), FlowInstanceMappingType.TERMINATED);
        } else if (runtimeResult.getStatus() == FlowInstanceStatus.END) {
            // The subFlow is completed from the EndNode to the MainFlow
            currentNodeInstance.setStatus(NodeInstanceStatus.COMPLETED);
            // transfer data from subFlow to MainFlow
            saveCallActivityEndInstanceData(runtimeContext, runtimeResult);
        }
    }

    private void saveCallActivityEndInstanceData(RuntimeContext runtimeContext, RuntimeResult runtimeResult) throws ProcessException {
        NodeInstanceBo currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        List<InstanceDataVo> instanceDataFromSubFlow = calculateCallActivityOutParamFromSubFlow(runtimeContext, runtimeResult.getVariables());
        // 1.merge to current data
        Map<String, InstanceDataVo> currentInstanceDataMap = runtimeContext.getInstanceDataMap();
        currentInstanceDataMap.putAll(InstanceDataUtil.getInstanceDataMap(instanceDataFromSubFlow));
        // 2.save data
        String instanceDataId = genId();
        InstanceData instanceData = buildCallActivityEndInstanceData(instanceDataId, runtimeContext);
        instanceDataService.save(instanceData);
        runtimeContext.setInstanceDataId(instanceDataId);
        // 3.set currentNode completed
        currentNodeInstance.setInstanceDataId(runtimeContext.getInstanceDataId());
    }
}
