package cn.sinozg.applet.turbo.engine.processor;

import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.turbo.engine.bo.ElementInstance;
import cn.sinozg.applet.turbo.engine.bo.FlowInfo;
import cn.sinozg.applet.turbo.engine.bo.FlowInstanceBo;
import cn.sinozg.applet.turbo.engine.bo.NodeInstanceBo;
import cn.sinozg.applet.turbo.engine.bo.NodeInstanceMidBo;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.common.FlowInstanceMappingType;
import cn.sinozg.applet.turbo.engine.common.FlowInstanceStatus;
import cn.sinozg.applet.turbo.engine.common.NodeInstanceStatus;
import cn.sinozg.applet.turbo.engine.common.ProcessStatus;
import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.exception.ReentrantException;
import cn.sinozg.applet.turbo.engine.exception.TurboException;
import cn.sinozg.applet.turbo.engine.executor.FlowExecutor;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;
import cn.sinozg.applet.turbo.engine.param.CommitTaskParam;
import cn.sinozg.applet.turbo.engine.param.RollbackTaskParam;
import cn.sinozg.applet.turbo.engine.param.StartProcessParam;
import cn.sinozg.applet.turbo.engine.result.CommitTaskResult;
import cn.sinozg.applet.turbo.engine.result.ElementInstanceListResult;
import cn.sinozg.applet.turbo.engine.result.FlowInstanceResult;
import cn.sinozg.applet.turbo.engine.result.InstanceDataListResult;
import cn.sinozg.applet.turbo.engine.result.NodeInstanceListResult;
import cn.sinozg.applet.turbo.engine.result.NodeInstanceResult;
import cn.sinozg.applet.turbo.engine.result.RollbackTaskResult;
import cn.sinozg.applet.turbo.engine.result.RuntimeResult;
import cn.sinozg.applet.turbo.engine.result.StartProcessResult;
import cn.sinozg.applet.turbo.engine.result.TerminateResult;
import cn.sinozg.applet.turbo.engine.service.FlowInstanceDataService;
import cn.sinozg.applet.turbo.engine.service.InstanceDataDataService;
import cn.sinozg.applet.turbo.engine.service.NodeInstanceDataService;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import cn.sinozg.applet.turbo.engine.util.InstanceDataUtil;
import cn.sinozg.applet.turbo.engine.validator.ParamValidator;
import cn.sinozg.applet.turbo.tb.entity.FlowDeployment;
import cn.sinozg.applet.turbo.tb.entity.FlowInstance;
import cn.sinozg.applet.turbo.tb.entity.FlowInstanceMapping;
import cn.sinozg.applet.turbo.tb.entity.InstanceData;
import cn.sinozg.applet.turbo.tb.entity.NodeInstance;
import cn.sinozg.applet.turbo.tb.service.FlowDeploymentService;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceMappingService;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceService;
import cn.sinozg.applet.turbo.tb.service.NodeInstanceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

@Component
public class RuntimeProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeProcessor.class);

    @Resource
    protected FlowDeploymentService flowDeploymentService;

    @Resource
    private FlowInstanceService flowInstanceService;

    @Resource
    private NodeInstanceService nodeInstanceService;

    @Resource
    private FlowInstanceMappingService flowInstanceMappingService;

    @Resource
    private FlowExecutor flowExecutor;

    @Resource
    private FlowInstanceDataService flowInstanceDataService;

    @Resource
    private InstanceDataDataService instanceDataDataService;

    @Resource
    private NodeInstanceDataService nodeInstanceDataService;

    ////////////////////////////////////////startProcess////////////////////////////////////////

    public StartProcessResult startProcess(StartProcessParam startProcessParam) {
        RuntimeContext runtimeContext = null;
        try {
            //1.param validate
            ParamValidator.validate(startProcessParam);

            //2.getFlowInfo
            FlowInfo flowInfo = getFlowInfo(startProcessParam);

            //3.init context for runtime
            runtimeContext = buildStartProcessContext(flowInfo, startProcessParam.getVariables(), startProcessParam.getRuntimeContext());

            //4.process
            flowExecutor.execute(runtimeContext);

            //5.build result
            return buildStartProcessResult(runtimeContext);
        } catch (TurboException e) {
            if (!ErrorEnum.isSuccess(e.getErrNo())) {
                LOGGER.warn("startProcess ProcessException.||startProcessParam={}||runtimeContext={}, ", startProcessParam, runtimeContext, e);
            }
            return buildStartProcessResult(runtimeContext, e);
        }
    }

    private FlowInfo getFlowInfo(StartProcessParam startProcessParam) throws ProcessException {
        if (StringUtils.isNotBlank(startProcessParam.getFlowDeployId())) {
            return getFlowInfoByFlowDeployId(startProcessParam.getFlowDeployId());
        } else {
            return getFlowInfoByFlowModuleId(startProcessParam.getFlowModuleId());
        }
    }

    /**
     * Init runtimeContext for startProcess:
     * 1.flowInfo: flowDeployId, flowModuleId, tenantId, flowModel(FlowElementList)
     * 2.variables: inputDataList fr. param
     */
    private RuntimeContext buildStartProcessContext(FlowInfo flowInfo, List<InstanceDataVo> variables, RuntimeContext parentRuntimeContext) {
        return buildRuntimeContext(flowInfo, variables, parentRuntimeContext);
    }

    private StartProcessResult buildStartProcessResult(RuntimeContext runtimeContext) {
        StartProcessResult startProcessResult = PojoUtil.copyBean(runtimeContext, StartProcessResult.class);
        return (StartProcessResult) fillRuntimeResult(startProcessResult, runtimeContext);
    }

    private StartProcessResult buildStartProcessResult(RuntimeContext runtimeContext, TurboException e) {
        StartProcessResult startProcessResult = PojoUtil.copyBean(runtimeContext, StartProcessResult.class);
        return (StartProcessResult) fillRuntimeResult(startProcessResult, runtimeContext, e);
    }

    ////////////////////////////////////////commit////////////////////////////////////////

    public CommitTaskResult commit(CommitTaskParam commitTaskParam) {
        RuntimeContext runtimeContext = null;
        try {
            //1.param validate
            ParamValidator.validate(commitTaskParam);

            //2.get flowInstance
            FlowInstanceBo flowInstanceBo = getFlowInstanceBo(commitTaskParam.getFlowInstanceId());

            //3.check status
            if (flowInstanceBo.getStatus() == FlowInstanceStatus.TERMINATED) {
                LOGGER.warn("commit failed: flowInstance has been completed.||commitTaskParam={}", commitTaskParam);
                throw new ProcessException(ErrorEnum.COMMIT_REJECTRD);
            }
            if (flowInstanceBo.getStatus() == FlowInstanceStatus.COMPLETED) {
                LOGGER.warn("commit: reentrant process.||commitTaskParam={}", commitTaskParam);
                throw new ReentrantException(ErrorEnum.REENTRANT_WARNING);
            }
            String flowDeployId = flowInstanceBo.getFlowDeployId();

            //4.getFlowInfo
            FlowInfo flowInfo = getFlowInfoByFlowDeployId(flowDeployId);

            //5.init runtimeContext
            runtimeContext = buildCommitContext(commitTaskParam, flowInfo, flowInstanceBo.getStatus());

            //6.process
            flowExecutor.commit(runtimeContext);

            //7.build result
            return buildCommitTaskResult(runtimeContext);
        } catch (TurboException e) {
            if (!ErrorEnum.isSuccess(e.getErrNo())) {
                LOGGER.warn("commit ProcessException.||commitTaskParam={}||runtimeContext={}, ", commitTaskParam, runtimeContext, e);
            }
            return buildCommitTaskResult(runtimeContext, e);
        }
    }

    private RuntimeContext buildCommitContext(CommitTaskParam commitTaskParam, FlowInfo flowInfo, int flowInstanceStatus) {
        //1. set flow info
        RuntimeContext runtimeContext = buildRuntimeContext(flowInfo, commitTaskParam.getVariables(), commitTaskParam.getRuntimeContext());

        //2. init flowInstance with flowInstanceId
        runtimeContext.setFlowInstanceId(commitTaskParam.getFlowInstanceId());
        runtimeContext.setFlowInstanceStatus(flowInstanceStatus);

        //3.set suspendNodeInstance stack
        RuntimeContext parentRuntimeContext = runtimeContext.getParentRuntimeContext();
        String realNodeInstanceId;
        if (parentRuntimeContext == null) {
            Stack<String> nodeInstanceId2RootStack = flowInstanceDataService.getNodeInstanceIdStack(commitTaskParam.getFlowInstanceId(), commitTaskParam.getTaskInstanceId());
            runtimeContext.setSuspendNodeInstanceStack(nodeInstanceId2RootStack);
            realNodeInstanceId = nodeInstanceId2RootStack.isEmpty() ? commitTaskParam.getTaskInstanceId() : nodeInstanceId2RootStack.pop();
        } else {
            runtimeContext.setSuspendNodeInstanceStack(parentRuntimeContext.getSuspendNodeInstanceStack());
            realNodeInstanceId = commitTaskParam.getTaskInstanceId();
        }

        //4. set suspendNodeInstance with taskInstance in param
        NodeInstanceBo suspendNodeInstance = new NodeInstanceBo();
        suspendNodeInstance.setNodeInstanceId(realNodeInstanceId);
        runtimeContext.setSuspendNodeInstance(suspendNodeInstance);

        //5. set callActivity msg
        runtimeContext.setCallActivityFlowModuleId(commitTaskParam.getCallActivityFlowModuleId());

        return runtimeContext;
    }

    private CommitTaskResult buildCommitTaskResult(RuntimeContext runtimeContext) {
        CommitTaskResult commitTaskResult = new CommitTaskResult();
        return (CommitTaskResult) fillRuntimeResult(commitTaskResult, runtimeContext);
    }

    private CommitTaskResult buildCommitTaskResult(RuntimeContext runtimeContext, TurboException e) {
        CommitTaskResult commitTaskResult = new CommitTaskResult();
        return (CommitTaskResult) fillRuntimeResult(commitTaskResult, runtimeContext, e);
    }

    ////////////////////////////////////////rollback////////////////////////////////////////

    /**
     * Rollback: rollback node process from param.taskInstance to the last taskInstance to suspend
     *
     * @param rollbackTaskParam: flowInstanceId + taskInstanceId(nodeInstanceId)
     * @return rollbackTaskResult: runtimeResult, flowInstanceId + activeTaskInstance(nodeInstanceId,nodeKey,status) + dataMap
     * @throws Exception
     */
    public RollbackTaskResult rollback(RollbackTaskParam rollbackTaskParam) {
        RuntimeContext runtimeContext = null;
        try {
            //1.param validate
            ParamValidator.validate(rollbackTaskParam);

            //2.get flowInstance
            FlowInstanceBo flowInstanceBo = getFlowInstanceBo(rollbackTaskParam.getFlowInstanceId());

            //3.check status
            if ((flowInstanceBo.getStatus() != FlowInstanceStatus.RUNNING) && (flowInstanceBo.getStatus() != FlowInstanceStatus.END)) {
                LOGGER.warn("rollback failed: invalid status to rollback.||rollbackTaskParam={}||status={}",
                    rollbackTaskParam, flowInstanceBo.getStatus());
                throw new ProcessException(ErrorEnum.ROLLBACK_REJECTRD);
            }
            String flowDeployId = flowInstanceBo.getFlowDeployId();

            //4.getFlowInfo
            FlowInfo flowInfo = getFlowInfoByFlowDeployId(flowDeployId);

            //5.init runtimeContext
            runtimeContext = buildRollbackContext(rollbackTaskParam, flowInfo, flowInstanceBo.getStatus());

            //6.process
            flowExecutor.rollback(runtimeContext);

            //7.build result
            return buildRollbackTaskResult(runtimeContext);
        } catch (TurboException e) {
            if (!ErrorEnum.isSuccess(e.getErrNo())) {
                LOGGER.warn("rollback ProcessException.||rollbackTaskParam={}||runtimeContext={}, ", rollbackTaskParam, runtimeContext, e);
            }
            return buildRollbackTaskResult(runtimeContext, e);
        }
    }

    private RuntimeContext buildRollbackContext(RollbackTaskParam rollbackTaskParam, FlowInfo flowInfo, int flowInstanceStatus) {
        //1. set flow info
        RuntimeContext runtimeContext = buildRuntimeContext(flowInfo);

        //2. init flowInstance with flowInstanceId
        runtimeContext.setFlowInstanceId(rollbackTaskParam.getFlowInstanceId());
        runtimeContext.setFlowInstanceStatus(flowInstanceStatus);

        //3.set suspendNodeInstance stack
        RuntimeContext parentRuntimeContext = rollbackTaskParam.getRuntimeContext();
        String realNodeInstanceId = null;
        if (parentRuntimeContext == null) {
            Stack<String> nodeInstanceId2RootStack = flowInstanceDataService.getNodeInstanceIdStack(rollbackTaskParam.getFlowInstanceId(), rollbackTaskParam.getTaskInstanceId());
            runtimeContext.setSuspendNodeInstanceStack(nodeInstanceId2RootStack);
            realNodeInstanceId = nodeInstanceId2RootStack.isEmpty() ? rollbackTaskParam.getTaskInstanceId() : nodeInstanceId2RootStack.pop();
        } else {
            runtimeContext.setParentRuntimeContext(rollbackTaskParam.getRuntimeContext());
            runtimeContext.setSuspendNodeInstanceStack(rollbackTaskParam.getRuntimeContext().getSuspendNodeInstanceStack());
            realNodeInstanceId = rollbackTaskParam.getTaskInstanceId();
        }

        //3. set suspendNodeInstance with taskInstance in param
        NodeInstanceBo suspendNodeInstance = new NodeInstanceBo();
        suspendNodeInstance.setNodeInstanceId(realNodeInstanceId);
        runtimeContext.setSuspendNodeInstance(suspendNodeInstance);

        return runtimeContext;
    }

    private RollbackTaskResult buildRollbackTaskResult(RuntimeContext runtimeContext) {
        RollbackTaskResult rollbackTaskResult = new RollbackTaskResult();
        return (RollbackTaskResult) fillRuntimeResult(rollbackTaskResult, runtimeContext);
    }

    private RollbackTaskResult buildRollbackTaskResult(RuntimeContext runtimeContext, TurboException e) {
        RollbackTaskResult rollbackTaskResult = new RollbackTaskResult();
        return (RollbackTaskResult) fillRuntimeResult(rollbackTaskResult, runtimeContext, e);
    }

    ////////////////////////////////////////terminate////////////////////////////////////////

    public TerminateResult terminateProcess(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        TerminateResult terminateResult;
        try {
            int flowInstanceStatus;

            FlowInstance flowInstance = flowInstanceService.selectByFlowInstanceId(flowInstanceId);
            if (flowInstance == null) {
                LOGGER.warn("terminateProcess failed: cannot find flowInstancePO from db.||flowInstanceId={}", flowInstanceId);
                throw new ProcessException(ErrorEnum.GET_FLOW_INSTANCE_FAILED);
            }

            if (flowInstance.getStatus() == FlowInstanceStatus.COMPLETED) {
                LOGGER.warn("terminateProcess: flowInstance is completed.||flowInstanceId={}", flowInstanceId);
                flowInstanceStatus = FlowInstanceStatus.COMPLETED;
            } else {
                flowInstanceService.updateStatus(flowInstance, FlowInstanceStatus.TERMINATED);
                flowInstanceStatus = FlowInstanceStatus.TERMINATED;
            }

            if (effectiveForSubFlowInstance) {
                terminateSubFlowInstance(flowInstanceId);
            }

            terminateResult = new TerminateResult(ErrorEnum.SUCCESS);
            terminateResult.setFlowInstanceId(flowInstanceId);
            terminateResult.setStatus(flowInstanceStatus);
        } catch (Exception e) {
            LOGGER.error("terminateProcess exception.||flowInstanceId={}, ", flowInstanceId, e);
            terminateResult = new TerminateResult(ErrorEnum.SYSTEM_ERROR);
            terminateResult.setFlowInstanceId(flowInstanceId);
        }
        return terminateResult;
    }

    public void terminateSubFlowInstance(String flowInstanceId) {
        Set<String> allSubFlowInstanceIds = flowInstanceDataService.getAllSubFlowInstanceIds(flowInstanceId);
        for (String subFlowInstanceId : allSubFlowInstanceIds) {
            terminateProcess(subFlowInstanceId, false);
        }
    }

    ////////////////////////////////////////getHistoryUserTaskList////////////////////////////////////////

    public NodeInstanceListResult getHistoryUserTaskList(String flowInstanceId, boolean effectiveForSubFlowInstance) {

        //1.get nodeInstanceList by flowInstanceId order by id desc
        List<NodeInstance> historyNodeInstanceList = getDescHistoryNodeInstanceList(flowInstanceId);

        //2.init result
        NodeInstanceListResult historyListResult = new NodeInstanceListResult(ErrorEnum.SUCCESS);
        historyListResult.setNodeInstanceList(new ArrayList<>());

        try {

            if (CollectionUtils.isEmpty(historyNodeInstanceList)) {
                LOGGER.warn("getHistoryUserTaskList: historyNodeInstanceList is empty.||flowInstanceId={}", flowInstanceId);
                return historyListResult;
            }

            //3.get flow info
            String flowDeployId = historyNodeInstanceList.get(0).getFlowDeployId();
            Map<String, FlowElement> flowElementMap = getFlowElementMap(flowDeployId);

            //4.pick out userTask and build result //empty list
            List<NodeInstanceMidBo> userTaskList = historyListResult.getNodeInstanceList();

            for (NodeInstance nodeInstance : historyNodeInstanceList) {
                //ignore noneffective nodeInstance
                if (!isEffectiveNodeInstance(nodeInstance.getStatus())) {
                    continue;
                }

                if (effectiveForSubFlowInstance && isCallActivity(nodeInstance.getNodeKey(), flowElementMap)) {
                    //handle subFlowInstance
                    String subFlowInstanceId = getExecuteSubFlowInstanceId(flowInstanceId, nodeInstance.getNodeInstanceId());
                    if (StringUtils.isNotBlank(subFlowInstanceId)) {
                        NodeInstanceListResult historyUserTaskList = getHistoryUserTaskList(subFlowInstanceId, true);
                        userTaskList.addAll(historyUserTaskList.getNodeInstanceList());
                    }
                    continue;
                }

                //ignore un-userTask instance
                if (!isUserTask(nodeInstance.getNodeKey(), flowElementMap)) {
                    continue;
                }

                //build effective userTask instance
                NodeInstanceMidBo ni = PojoUtil.copyBean(nodeInstance, NodeInstanceMidBo.class);
                //set instanceId & status

                //set ElementModel info
                FlowElement flowElement = FlowModelUtil.getFlowElement(flowElementMap, nodeInstance.getNodeKey());
                ni.setModelKey(flowElement.getKey());
                ni.setModelName(FlowModelUtil.getElementName(flowElement));
                if (MapUtils.isNotEmpty(flowElement.getProperties())) {
                    ni.setProperties(flowElement.getProperties());
                } else {
                    ni.setProperties(new HashMap<>(16));
                }
                userTaskList.add(ni);
            }
        } catch (ProcessException e) {
            historyListResult.setErrCode(e.getErrNo());
            historyListResult.setErrMsg(e.getErrMsg());
        }
        return historyListResult;
    }

    private Map<String, FlowElement> getFlowElementMap(String flowDeployId) throws ProcessException {
        FlowInfo flowInfo = getFlowInfoByFlowDeployId(flowDeployId);
        String flowModel = flowInfo.getFlowModel();
        return FlowModelUtil.getFlowElementMap(flowModel);
    }

    private boolean isEffectiveNodeInstance(int status) {
        return status == NodeInstanceStatus.COMPLETED || status == NodeInstanceStatus.ACTIVE;
    }

    private boolean isUserTask(String nodeKey, Map<String, FlowElement> flowElementMap) throws ProcessException {
        int type = getNodeType(nodeKey, flowElementMap);
        return type == FlowElementType.USER_TASK;
    }

    private int getNodeType(String nodeKey, Map<String, FlowElement> flowElementMap) throws ProcessException {
        if (!flowElementMap.containsKey(nodeKey)) {
            LOGGER.warn("isUserTask: invalid nodeKey which is not in flowElementMap.||nodeKey={}||flowElementMap={}",
                nodeKey, flowElementMap);
            throw new ProcessException(ErrorEnum.GET_NODE_FAILED);
        }
        FlowElement flowElement = flowElementMap.get(nodeKey);
        return flowElement.getType();
    }

    private boolean isCallActivity(String nodeKey, Map<String, FlowElement> flowElementMap) throws ProcessException {
        int type = getNodeType(nodeKey, flowElementMap);
        return type == FlowElementType.CALL_ACTIVITY;
    }

    ////////////////////////////////////////getHistoryElementList////////////////////////////////////////

    public ElementInstanceListResult getHistoryElementList(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        //1.getHistoryNodeList
        List<NodeInstance> historyNodeInstanceList = getHistoryNodeInstanceList(flowInstanceId);

        //2.init
        ElementInstanceListResult elementInstanceListResult = new ElementInstanceListResult(ErrorEnum.SUCCESS);
        elementInstanceListResult.setElementInstanceList(new ArrayList<>());

        try {
            if (CollectionUtils.isEmpty(historyNodeInstanceList)) {
                LOGGER.warn("getHistoryElementList: historyNodeInstanceList is empty.||flowInstanceId={}", flowInstanceId);
                return elementInstanceListResult;
            }

            //3.get flow info
            String flowDeployId = historyNodeInstanceList.get(0).getFlowDeployId();
            Map<String, FlowElement> flowElementMap = getFlowElementMap(flowDeployId);

            //4.calculate elementInstanceMap: key=elementKey, value(lasted)=ElementInstance(elementKey, status)
            List<ElementInstance> elementInstanceList = elementInstanceListResult.getElementInstanceList();
            for (NodeInstance nodeInstance : historyNodeInstanceList) {
                String nodeKey = nodeInstance.getNodeKey();
                String sourceNodeKey = nodeInstance.getSourceNodeKey();
                int nodeStatus = nodeInstance.getStatus();
                String nodeInstanceId = nodeInstance.getNodeInstanceId();
                String instanceDataId = nodeInstance.getInstanceDataId();
                //4.1 build the source sequenceFlow instance
                if (StringUtils.isNotBlank(sourceNodeKey)) {
                    FlowElement sourceFlowElement = FlowModelUtil.getSequenceFlow(flowElementMap, sourceNodeKey, nodeKey);
                    if (sourceFlowElement == null) {
                        LOGGER.error("getHistoryElementList failed: sourceFlowElement is null."
                            + "||nodeKey={}||sourceNodeKey={}||flowElementMap={}", nodeKey, sourceNodeKey, flowElementMap);
                        throw new ProcessException(ErrorEnum.MODEL_UNKNOWN_ELEMENT_KEY);
                    }

                    //build ElementInstance
                    int sourceSequenceFlowStatus = nodeStatus;
                    if (nodeStatus == NodeInstanceStatus.ACTIVE) {
                        sourceSequenceFlowStatus = NodeInstanceStatus.COMPLETED;
                    }
                    ElementInstance sequenceFlowInstance = new ElementInstance(sourceFlowElement.getKey(), sourceSequenceFlowStatus, null, null);
                    elementInstanceList.add(sequenceFlowInstance);
                }

                //4.2 build nodeInstance
                ElementInstance ei = new ElementInstance(nodeKey, nodeStatus, nodeInstanceId, instanceDataId);
                elementInstanceList.add(ei);

                //4.3 handle callActivity
                if (!FlowModelUtil.isElementType(nodeKey, flowElementMap, FlowElementType.CALL_ACTIVITY)) {
                    continue;
                }
                if (!effectiveForSubFlowInstance) {
                    continue;
                }
                List<FlowInstanceMapping> flowInstanceMappingList = flowInstanceMappingService.selectFlowInstanceMappingList(flowInstanceId, nodeInstanceId);
                List<ElementInstance> subElementInstanceList = new ArrayList<>();
                ei.setSubElementInstanceList(subElementInstanceList);
                for (FlowInstanceMapping flowInstanceMapping : flowInstanceMappingList) {
                    ElementInstanceListResult subElementInstanceListResult = getHistoryElementList(flowInstanceMapping.getSubFlowInstanceId(), effectiveForSubFlowInstance);
                    subElementInstanceList.addAll(subElementInstanceListResult.getElementInstanceList());
                }
            }
        } catch (ProcessException e) {
            elementInstanceListResult.setErrCode(e.getErrNo());
            elementInstanceListResult.setErrMsg(e.getErrMsg());
        }
        return elementInstanceListResult;
    }

    private String getExecuteSubFlowInstanceId(String flowInstanceId, String nodeInstanceId) {
        List<FlowInstanceMapping> flowInstanceMappingList = flowInstanceMappingService.selectFlowInstanceMappingList(flowInstanceId, nodeInstanceId);
        if (CollectionUtils.isEmpty(flowInstanceMappingList)) {
            return null;
        }
        for (FlowInstanceMapping flowInstanceMapping : flowInstanceMappingList) {
            if (FlowInstanceMappingType.EXECUTE == flowInstanceMapping.getType()) {
                return flowInstanceMapping.getSubFlowInstanceId();
            }
        }
        return flowInstanceMappingList.get(0).getSubFlowInstanceId();
    }

    private List<NodeInstance> getHistoryNodeInstanceList(String flowInstanceId) {
        return nodeInstanceService.selectByFlowInstanceId(flowInstanceId);
    }

    private List<NodeInstance> getDescHistoryNodeInstanceList(String flowInstanceId) {
        return nodeInstanceService.selectDescByFlowInstanceId(flowInstanceId);
    }

    public NodeInstanceResult getNodeInstance(String flowInstanceId, String nodeInstanceId, boolean effectiveForSubFlowInstance) {
        NodeInstanceResult nodeInstanceResult = new NodeInstanceResult();
        try {
            NodeInstance nodeInstance = nodeInstanceDataService.selectByNodeInstanceId(flowInstanceId, nodeInstanceId, effectiveForSubFlowInstance);
            String flowDeployId = nodeInstance.getFlowDeployId();
            Map<String, FlowElement> flowElementMap = getFlowElementMap(flowDeployId);
            NodeInstanceMidBo nodeInstanceMid = PojoUtil.copyBean(nodeInstance, NodeInstanceMidBo.class);
            FlowElement flowElement = FlowModelUtil.getFlowElement(flowElementMap, nodeInstance.getNodeKey());
            if (flowElement.getType() == FlowElementType.CALL_ACTIVITY) {
                List<FlowInstanceMapping> flowInstanceMappingList = flowInstanceMappingService.selectFlowInstanceMappingList(flowInstanceId, nodeInstanceId);
                List<String> subFlowInstanceIdList = new ArrayList<>();
                for (FlowInstanceMapping flowInstanceMapping : flowInstanceMappingList) {
                    subFlowInstanceIdList.add(flowInstanceMapping.getSubFlowInstanceId());
                }
                nodeInstanceMid.setSubFlowInstanceIdList(subFlowInstanceIdList);
            }
            nodeInstanceMid.setModelKey(flowElement.getKey());
            nodeInstanceMid.setModelName(FlowModelUtil.getElementName(flowElement));
            if (MapUtils.isNotEmpty(flowElement.getProperties())) {
                nodeInstanceMid.setProperties(flowElement.getProperties());
            } else {
                nodeInstanceMid.setProperties(new HashMap<>(16));
            }
            nodeInstanceResult.setNodeInstance(nodeInstanceMid);
            nodeInstanceResult.setErrCode(ErrorEnum.SUCCESS.getErrNo());
            nodeInstanceResult.setErrMsg(ErrorEnum.SUCCESS.getErrMsg());
        } catch (ProcessException e) {
            nodeInstanceResult.setErrCode(e.getErrNo());
            nodeInstanceResult.setErrMsg(e.getErrMsg());
        }
        return nodeInstanceResult;
    }

    ////////////////////////////////////////getInstanceData////////////////////////////////////////
    public InstanceDataListResult getInstanceData(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        InstanceData instanceData = instanceDataDataService.select(flowInstanceId, effectiveForSubFlowInstance);
        return packageInstanceDataResult(instanceData);
    }

    public InstanceDataListResult getInstanceData(String flowInstanceId, String instanceDataId, boolean effectiveForSubFlowInstance) {
        InstanceData instanceData = instanceDataDataService.select(flowInstanceId, instanceDataId, effectiveForSubFlowInstance);
        return packageInstanceDataResult(instanceData);
    }

    public InstanceDataListResult packageInstanceDataResult(InstanceData instanceData) {
        List<InstanceDataVo> instanceDataList = JsonUtil.toPojos(instanceData.getInstanceData(), InstanceDataVo.class);
        if (CollectionUtils.isEmpty(instanceDataList)) {
            instanceDataList = new ArrayList<>();
        }

        InstanceDataListResult instanceDataListResult = new InstanceDataListResult(ErrorEnum.SUCCESS);
        instanceDataListResult.setVariables(instanceDataList);
        return instanceDataListResult;
    }


    public FlowInstanceResult getFlowInstance(String flowInstanceId) {
        FlowInstanceResult flowInstanceResult = new FlowInstanceResult();
        try {
            FlowInstanceBo flowInstanceBo = getFlowInstanceBo(flowInstanceId);
            flowInstanceResult.setFlowInstanceBo(flowInstanceBo);
        } catch (ProcessException e) {
            flowInstanceResult.setErrCode(e.getErrNo());
            flowInstanceResult.setErrMsg(e.getErrMsg());
        }
        return flowInstanceResult;
    }


    ////////////////////////////////////////common////////////////////////////////////////

    private FlowInfo getFlowInfoByFlowDeployId(String flowDeployId) throws ProcessException {

        FlowDeployment flowDeployment = flowDeploymentService.selectByDeployId(flowDeployId);
        if (flowDeployment == null) {
            LOGGER.warn("getFlowInfoByFlowDeployId failed.||flowDeployId={}", flowDeployId);
            throw new ProcessException(ErrorEnum.GET_FLOW_DEPLOYMENT_FAILED);
        }

        return PojoUtil.copyBean(flowDeployment, FlowInfo.class);
    }

    private FlowInfo getFlowInfoByFlowModuleId(String flowModuleId) throws ProcessException {
        //get from db directly
        FlowDeployment flowDeployment = flowDeploymentService.selectRecentByFlowModuleId(flowModuleId);
        if (flowDeployment == null) {
            LOGGER.warn("getFlowInfoByFlowModuleId failed.||flowModuleId={}", flowModuleId);
            throw new ProcessException(ErrorEnum.GET_FLOW_DEPLOYMENT_FAILED);
        }
        return PojoUtil.copyBean(flowDeployment, FlowInfo.class);
    }

    private FlowInstanceBo getFlowInstanceBo(String flowInstanceId) throws ProcessException {
        //get from db
        FlowInstance flowInstance = flowInstanceService.selectByFlowInstanceId(flowInstanceId);
        if (flowInstance == null) {
            LOGGER.warn("getFlowInstancePO failed: cannot find flowInstancePO from db.||flowInstanceId={}", flowInstanceId);
            throw new ProcessException(ErrorEnum.GET_FLOW_INSTANCE_FAILED);
        }
        return PojoUtil.copyBean(flowInstance, FlowInstanceBo.class);
    }

    private RuntimeContext buildRuntimeContext(FlowInfo flowInfo) {
        RuntimeContext runtimeContext = PojoUtil.copyBean(flowInfo, RuntimeContext.class);
        runtimeContext.setFlowElementMap(FlowModelUtil.getFlowElementMap(flowInfo.getFlowModel()));
        return runtimeContext;
    }

    private RuntimeContext buildRuntimeContext(FlowInfo flowInfo, List<InstanceDataVo> variables, RuntimeContext parentRuntimeContext) {
        RuntimeContext runtimeContext = buildRuntimeContext(flowInfo);
        Map<String, InstanceDataVo> instanceDataMap = InstanceDataUtil.getInstanceDataMap(variables);
        runtimeContext.setInstanceDataMap(instanceDataMap);
        runtimeContext.setParentRuntimeContext(parentRuntimeContext);
        return runtimeContext;
    }

    private RuntimeResult fillRuntimeResult(RuntimeResult runtimeResult, RuntimeContext runtimeContext) {
        if (runtimeContext.getProcessStatus() == ProcessStatus.SUCCESS) {
            return fillRuntimeResult(runtimeResult, runtimeContext, ErrorEnum.SUCCESS);
        }
        return fillRuntimeResult(runtimeResult, runtimeContext, ErrorEnum.FAILED);
    }

    private RuntimeResult fillRuntimeResult(RuntimeResult runtimeResult, RuntimeContext runtimeContext, ErrorEnum errorEnum) {
        return fillRuntimeResult(runtimeResult, runtimeContext, errorEnum.getErrNo(), errorEnum.getErrMsg());
    }

    private RuntimeResult fillRuntimeResult(RuntimeResult runtimeResult, RuntimeContext runtimeContext, TurboException e) {
        return fillRuntimeResult(runtimeResult, runtimeContext, e.getErrNo(), e.getErrMsg());
    }

    private RuntimeResult fillRuntimeResult(RuntimeResult runtimeResult, RuntimeContext runtimeContext, int errNo, String errMsg) {
        runtimeResult.setErrCode(errNo);
        runtimeResult.setErrMsg(errMsg);

        if (runtimeContext != null) {
            runtimeResult.setFlowInstanceId(runtimeContext.getFlowInstanceId());
            runtimeResult.setStatus(runtimeContext.getFlowInstanceStatus());
            runtimeResult.setActiveTaskInstance(buildActiveTaskInstance(runtimeContext.getSuspendNodeInstance(), runtimeContext));
            runtimeResult.setVariables(InstanceDataUtil.getInstanceDataList(runtimeContext.getInstanceDataMap()));
        }
        return runtimeResult;
    }

    private NodeInstanceMidBo buildActiveTaskInstance(NodeInstanceBo nodeInstance, RuntimeContext runtimeContext) {
        NodeInstanceMidBo activeNodeInstance = PojoUtil.copyBean(nodeInstance, NodeInstanceMidBo.class);
        activeNodeInstance.setModelKey(nodeInstance.getNodeKey());
        FlowElement flowElement = runtimeContext.getFlowElementMap().get(nodeInstance.getNodeKey());
        activeNodeInstance.setModelName(FlowModelUtil.getElementName(flowElement));
        activeNodeInstance.setProperties(flowElement.getProperties());
        activeNodeInstance.setFlowElementType(flowElement.getType());
        activeNodeInstance.setSubNodeResultList(runtimeContext.getCallActivityRuntimeResultList());


        return activeNodeInstance;
    }

    public void checkIsSubFlowInstance(String flowInstanceId) {
        FlowInstance flowInstance = flowInstanceService.selectByFlowInstanceId(flowInstanceId);
        if (flowInstance == null) {
            LOGGER.warn("checkIsSubFlowInstance failed: cannot find flowInstancePO from db.||flowInstanceId={}", flowInstanceId);
            throw new RuntimeException(ErrorEnum.GET_FLOW_INSTANCE_FAILED.getErrMsg());
        }
        if (StringUtils.isNotBlank(flowInstance.getParentFlowInstanceId())) {
            LOGGER.error("checkIsSubFlowInstance failed: don't receive sub-processes.||flowInstanceId={}", flowInstanceId);
            throw new RuntimeException(ErrorEnum.NO_RECEIVE_SUB_FLOW_INSTANCE.getErrMsg());
        }
    }

}
