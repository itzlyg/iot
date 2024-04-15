package cn.sinozg.applet.turbo.engine.executor;

import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.turbo.engine.bo.NodeInstanceBo;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.common.FlowInstanceStatus;
import cn.sinozg.applet.turbo.engine.common.InstanceDataType;
import cn.sinozg.applet.turbo.engine.common.NodeInstanceStatus;
import cn.sinozg.applet.turbo.engine.common.NodeInstanceType;
import cn.sinozg.applet.turbo.engine.common.ProcessStatus;
import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import cn.sinozg.applet.turbo.engine.exception.ProcessException;
import cn.sinozg.applet.turbo.engine.exception.ReentrantException;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import cn.sinozg.applet.turbo.engine.util.InstanceDataUtil;
import cn.sinozg.applet.turbo.tb.entity.FlowInstance;
import cn.sinozg.applet.turbo.tb.entity.InstanceData;
import cn.sinozg.applet.turbo.tb.entity.NodeInstance;
import cn.sinozg.applet.turbo.tb.entity.NodeInstanceLog;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlowExecutor extends AbstractRuntimeExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowExecutor.class);

    @Resource
    private FlowInstanceService flowInstanceService;

    ////////////////////////////////////////execute////////////////////////////////////////

    @Override
    public void execute(RuntimeContext runtimeContext) throws ProcessException {
        int processStatus = ProcessStatus.SUCCESS;
        try {
            preExecute(runtimeContext);
            doExecute(runtimeContext);
        } catch (ProcessException pe) {
            if (!ErrorEnum.isSuccess(pe.getErrNo())) {
                processStatus = ProcessStatus.FAILED;
            }
            throw pe;
        } finally {
            runtimeContext.setProcessStatus(processStatus);
            postExecute(runtimeContext);
        }
    }

    /**
     * Fill runtimeContext:
     * 1. Generate flowInstanceId and insert FlowInstancePO into db
     * 2. Generate instanceDataId and insert InstanceDataPO into db
     * 3. Update runtimeContext: flowInstanceId, flowInstanceStatus, instanceDataId, nodeInstanceList, suspendNodeInstance
     *
     * @throws Exception
     */
    private void preExecute(RuntimeContext runtimeContext) throws ProcessException {
        //1.save FlowInstance into db
        FlowInstance flowInstance = saveFlowInstance(runtimeContext);

        //2.save InstanceData into db
        String instanceDataId = saveInstanceData(flowInstance, runtimeContext.getInstanceDataMap());

        //3.update runtimeContext
        fillExecuteContext(runtimeContext, flowInstance.getFlowInstanceId(), instanceDataId);
    }

    private FlowInstance saveFlowInstance(RuntimeContext runtimeContext) throws ProcessException {
        FlowInstance flowInstance = buildFlowInstance(runtimeContext);
        boolean result = flowInstanceService.save(flowInstance);
        if (result) {
            return flowInstance;
        }
        LOGGER.warn("saveFlowInstancePO: insert failed.||flowInstancePO={}", flowInstance);
        throw new ProcessException(ErrorEnum.SAVE_FLOW_INSTANCE_FAILED);
    }

    private FlowInstance buildFlowInstance(RuntimeContext runtimeContext) {
        FlowInstance flowInstance = PojoUtil.copyBean(runtimeContext, FlowInstance.class);
        // generate flowInstanceId
        flowInstance.setFlowInstanceId(genId());
        RuntimeContext parentRuntimeContext = runtimeContext.getParentRuntimeContext();
        if (parentRuntimeContext != null) {
            flowInstance.setParentFlowInstanceId(parentRuntimeContext.getFlowInstanceId());
        }
        flowInstance.setStatus(FlowInstanceStatus.RUNNING);
        return flowInstance;
    }

    private String saveInstanceData(FlowInstance flowInstance, Map<String, InstanceDataVo> instanceDataMap) throws ProcessException {
        if (MapUtils.isEmpty(instanceDataMap)) {
            return StringUtils.EMPTY;
        }

        InstanceData instanceData = buildInstanceDataPo(flowInstance, instanceDataMap);
        boolean result = instanceDataService.save(instanceData);
        if (result) {
            return instanceData.getInstanceDataId();
        }

        LOGGER.warn("saveInstanceDataPO: insert failed.||instanceDataPO={}", instanceData);
        throw new ProcessException(ErrorEnum.SAVE_INSTANCE_DATA_FAILED);
    }

    private InstanceData buildInstanceDataPo(FlowInstance flowInstance, Map<String, InstanceDataVo> instanceDataMap) {
        InstanceData instanceData = PojoUtil.copyBean(flowInstance, InstanceData.class);
        // copy flow info & flowInstanceId

        // generate instanceDataId
        instanceData.setInstanceDataId(genId());
        instanceData.setInstanceData(InstanceDataUtil.getInstanceDataListStr(instanceDataMap));

        instanceData.setNodeInstanceId(StringUtils.EMPTY);
        instanceData.setNodeKey(StringUtils.EMPTY);
        instanceData.setType(InstanceDataType.INIT);
        return instanceData;
    }

    private void fillExecuteContext(RuntimeContext runtimeContext, String flowInstanceId, String instanceDataId) throws ProcessException {
        runtimeContext.setFlowInstanceId(flowInstanceId);
        runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.RUNNING);

        runtimeContext.setInstanceDataId(instanceDataId);

        runtimeContext.setNodeInstanceList(new ArrayList<>());

        //set startEvent into suspendNodeInstance as the first node to process
        Map<String, FlowElement> flowElementMap = runtimeContext.getFlowElementMap();
        FlowElement startEvent = FlowModelUtil.getStartEvent(flowElementMap);
        if (startEvent == null) {
            LOGGER.warn("fillExecuteContext failed: cannot get startEvent.||flowInstance={}||flowDeployId={}",
                    runtimeContext.getFlowInstanceId(), runtimeContext.getFlowDeployId());
            throw new ProcessException(ErrorEnum.GET_NODE_FAILED);
        }
        NodeInstanceBo suspendNodeInstance = new NodeInstanceBo();
        suspendNodeInstance.setNodeKey(startEvent.getKey());
        suspendNodeInstance.setStatus(NodeInstanceStatus.ACTIVE);
        suspendNodeInstance.setSourceNodeInstanceId(StringUtils.EMPTY);
        suspendNodeInstance.setSourceNodeKey(StringUtils.EMPTY);
        runtimeContext.setSuspendNodeInstance(suspendNodeInstance);

        runtimeContext.setCurrentNodeModel(startEvent);
    }

    private void doExecute(RuntimeContext runtimeContext) throws ProcessException {
        AbstractRuntimeExecutor runtimeExecutor = getExecuteExecutor(runtimeContext);
        while (runtimeExecutor != null) {
            runtimeExecutor.execute(runtimeContext);
            runtimeExecutor = runtimeExecutor.getExecuteExecutor(runtimeContext);
        }
    }

    private void postExecute(RuntimeContext runtimeContext) throws ProcessException {

        //1.update context with processStatus
        if (runtimeContext.getProcessStatus() == ProcessStatus.SUCCESS) {
            //SUCCESS: update runtimeContext: update suspendNodeInstance
            if (runtimeContext.getCurrentNodeInstance() != null) {
                runtimeContext.setSuspendNodeInstance(runtimeContext.getCurrentNodeInstance());
            }
        }

        //2.save nodeInstanceList to db
        saveNodeInstanceList(runtimeContext, NodeInstanceType.EXECUTE);

        //3.update flowInstance status while completed
        if (isCompleted(runtimeContext)) {
            if (isSubFlowInstance(runtimeContext)) {
                flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.END);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.END);
            } else {
                flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.COMPLETED);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            }
            LOGGER.info("postExecute: flowInstance process completely.||flowInstanceId={}", runtimeContext.getFlowInstanceId());
        }
    }


    ////////////////////////////////////////commit////////////////////////////////////////

    @Override
    public void commit(RuntimeContext runtimeContext) throws ProcessException {
        int processStatus = ProcessStatus.SUCCESS;
        try {
            preCommit(runtimeContext);
            doCommit(runtimeContext);
        } catch (ReentrantException re) {
            //ignore
        } catch (ProcessException pe) {
            if (!ErrorEnum.isSuccess(pe.getErrNo())) {
                processStatus = ProcessStatus.FAILED;
            }
            throw pe;
        } finally {
            runtimeContext.setProcessStatus(processStatus);
            postCommit(runtimeContext);
        }
    }

    /**
     * Fill runtimeContext:
     * 1. Get instanceData from db firstly
     * 2. merge and save instanceData while commitData is not empty
     * 3. Update runtimeContext: instanceDataId, instanceDataMap, nodeInstanceList, suspendNodeInstance
     *
     * @throws Exception
     */
    private void preCommit(RuntimeContext runtimeContext) throws ProcessException {
        String flowInstanceId = runtimeContext.getFlowInstanceId();
        NodeInstanceBo suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        String nodeInstanceId = suspendNodeInstance.getNodeInstanceId();

        //1.get instanceData from db
        NodeInstance nodeInstance = nodeInstanceService.selectByNodeInstanceId(flowInstanceId, nodeInstanceId);
        if (nodeInstance == null) {
            LOGGER.warn("preCommit failed: cannot find nodeInstancePo from db.||flowInstanceId={}||nodeInstanceId={}",
                    flowInstanceId, nodeInstanceId);
            throw new ProcessException(ErrorEnum.GET_NODE_INSTANCE_FAILED);
        }

        //unexpected: flowInstance is completed
        if (isCompleted(runtimeContext)) {
            LOGGER.warn("preExecute warning: reentrant process. FlowInstance has been processed completely.||runtimeContext={}", runtimeContext);
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            suspendNodeInstance.setId(nodeInstance.getId());
            suspendNodeInstance.setNodeKey(nodeInstance.getNodeKey());
            suspendNodeInstance.setSourceNodeInstanceId(nodeInstance.getSourceNodeInstanceId());
            suspendNodeInstance.setSourceNodeKey(nodeInstance.getSourceNodeKey());
            suspendNodeInstance.setInstanceDataId(nodeInstance.getInstanceDataId());
            suspendNodeInstance.setStatus(nodeInstance.getStatus());
            throw new ReentrantException(ErrorEnum.REENTRANT_WARNING);
        }
        Map<String, InstanceDataVo> instanceDataMap;
        String instanceDataId = nodeInstance.getInstanceDataId();
        if (StringUtils.isBlank(instanceDataId)) {
            instanceDataMap = new HashMap<>(16);
        } else {
            InstanceData instanceData = instanceDataService.selectByIns(flowInstanceId, instanceDataId);
            if (instanceData == null) {
                LOGGER.warn("preCommit failed: cannot find instanceDataPO from db." +
                    "||flowInstanceId={}||instanceDataId={}", flowInstanceId, instanceDataId);
                throw new ProcessException(ErrorEnum.GET_INSTANCE_DATA_FAILED);
            }
            instanceDataMap = InstanceDataUtil.getInstanceDataMap(instanceData.getInstanceData());
        }

        //2.merge data while commitDataMap is not empty
        Map<String, InstanceDataVo> commitDataMap = runtimeContext.getInstanceDataMap();
        boolean isCallActivityNode = FlowModelUtil.isElementType(nodeInstance.getNodeKey(), runtimeContext.getFlowElementMap(), FlowElementType.CALL_ACTIVITY);
        if (isCallActivityNode) {
            // commit callActivity not allow merge data
            instanceDataMap = commitDataMap;
        } else if (MapUtils.isNotEmpty(commitDataMap)) {
            instanceDataId = genId();
            instanceDataMap.putAll(commitDataMap);

            InstanceData commitInstanceData = buildCommitInstanceData(runtimeContext, nodeInstanceId,
                    nodeInstance.getNodeKey(), instanceDataId, instanceDataMap);
            instanceDataService.save(commitInstanceData);
        }

        //3.update runtimeContext
        fillCommitContext(runtimeContext, nodeInstance, instanceDataId, instanceDataMap);
    }

    private InstanceData buildCommitInstanceData(RuntimeContext runtimeContext, String nodeInstanceId, String nodeKey,
                                                   String newInstanceDataId, Map<String, InstanceDataVo> instanceDataMap) {
        InstanceData instanceData = PojoUtil.copyBean(runtimeContext, InstanceData.class);

        instanceData.setNodeInstanceId(nodeInstanceId);
        instanceData.setNodeKey(nodeKey);
        instanceData.setType(InstanceDataType.COMMIT);

        instanceData.setInstanceDataId(newInstanceDataId);
        instanceData.setInstanceData(InstanceDataUtil.getInstanceDataListStr(instanceDataMap));

        return instanceData;
    }

    private void fillCommitContext(RuntimeContext runtimeContext, NodeInstance nodeInstance, String instanceDataId,
                                   Map<String, InstanceDataVo> instanceDataMap) throws ProcessException {

        runtimeContext.setInstanceDataId(instanceDataId);
        runtimeContext.setInstanceDataMap(instanceDataMap);

        updateSuspendNodeInstanceBO(runtimeContext.getSuspendNodeInstance(), nodeInstance, instanceDataId);

        setCurrentFlowModel(runtimeContext);

        runtimeContext.setNodeInstanceList(new ArrayList<>());
    }

    private void doCommit(RuntimeContext runtimeContext) throws ProcessException {
        AbstractRuntimeExecutor runtimeExecutor = getExecuteExecutor(runtimeContext);
        runtimeExecutor.commit(runtimeContext);

        runtimeExecutor = runtimeExecutor.getExecuteExecutor(runtimeContext);
        while (runtimeExecutor != null) {
            runtimeExecutor.execute(runtimeContext);
            runtimeExecutor = runtimeExecutor.getExecuteExecutor(runtimeContext);
        }
    }

    private void postCommit(RuntimeContext runtimeContext) throws ProcessException {
        if (runtimeContext.getProcessStatus() == ProcessStatus.SUCCESS && runtimeContext.getCurrentNodeInstance() != null) {
            runtimeContext.setSuspendNodeInstance(runtimeContext.getCurrentNodeInstance());
        }
        //update FlowInstancePO to db
        saveNodeInstanceList(runtimeContext, NodeInstanceType.COMMIT);

        if (isCompleted(runtimeContext)) {
            if (isSubFlowInstance(runtimeContext)) {
                flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.END);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.END);
            } else {
                flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.COMPLETED);
                runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            }

            LOGGER.info("postCommit: flowInstance process completely.||flowInstanceId={}", runtimeContext.getFlowInstanceId());
        }
    }

    ////////////////////////////////////////rollback////////////////////////////////////////

    @Override
    public void rollback(RuntimeContext runtimeContext) throws ProcessException {
        int processStatus = ProcessStatus.SUCCESS;
        try {
            preRollback(runtimeContext);
            doRollback(runtimeContext);
        } catch (ReentrantException re) {
            //ignore
        } catch (ProcessException pe) {
            if (!ErrorEnum.isSuccess(pe.getErrNo())) {
                processStatus = ProcessStatus.FAILED;
            }
            throw pe;
        } finally {
            runtimeContext.setProcessStatus(processStatus);
            postRollback(runtimeContext);
        }
    }

    private void preRollback(RuntimeContext runtimeContext) throws ProcessException {
        String flowInstanceId = runtimeContext.getFlowInstanceId();

        //1.check node: only the latest enabled(ACTIVE or COMPLETED) nodeInstance can be rollbacked.
        String suspendNodeInstanceId = runtimeContext.getSuspendNodeInstance().getNodeInstanceId();
        NodeInstance rollbackNodeInstance = getActiveNodeForRollback(flowInstanceId, suspendNodeInstanceId,
            runtimeContext.getFlowElementMap());
        if (rollbackNodeInstance == null) {
            LOGGER.warn("preRollback failed: cannot rollback.||runtimeContext={}", runtimeContext);
            throw new ProcessException(ErrorEnum.ROLLBACK_FAILED);
        }

        //2.check status: flowInstance is completed
        if (isCompleted(runtimeContext)) {
            LOGGER.warn("invalid preRollback: FlowInstance has been processed completely."
                + "||flowInstanceId={}||flowDeployId={}", flowInstanceId, runtimeContext.getFlowDeployId());
            NodeInstanceBo suspendNodeInstance = PojoUtil.copyBean(rollbackNodeInstance, NodeInstanceBo.class);
            runtimeContext.setSuspendNodeInstance(suspendNodeInstance);
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.COMPLETED);
            throw new ProcessException(ErrorEnum.ROLLBACK_FAILED);
        }

        //3.get instanceData
        String instanceDataId = rollbackNodeInstance.getInstanceDataId();
        Map<String, InstanceDataVo> instanceDataMap;
        if (StringUtils.isBlank(instanceDataId)) {
            instanceDataMap = new HashMap<>(16);
        } else {
            InstanceData instanceData = instanceDataService.selectByIns(flowInstanceId, instanceDataId);
            if (instanceData == null) {
                LOGGER.warn("preRollback failed: cannot find instanceDataPO from db."
                    + "||flowInstanceId={}||instanceDataId={}", flowInstanceId, instanceDataId);
                throw new ProcessException(ErrorEnum.GET_INSTANCE_DATA_FAILED);
            }
            instanceDataMap = InstanceDataUtil.getInstanceDataMap(instanceData.getInstanceData());
        }

        //4.update runtimeContext
        fillRollbackContext(runtimeContext, rollbackNodeInstance, instanceDataMap);
    }

    // if(canRollback): only the active Node or the lasted completed Node can be rollback
    private NodeInstance getActiveNodeForRollback(String flowInstanceId, String suspendNodeInstanceId,
                                                    Map<String, FlowElement> flowElementMap) {
        List<NodeInstance> nodeInstanceList = nodeInstanceService.selectDescByFlowInstanceId(flowInstanceId);
        if (CollectionUtils.isEmpty(nodeInstanceList)) {
            LOGGER.warn("getActiveNodeForRollback: nodeInstancePOList is empty."
                + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
            return null;
        }

        for (NodeInstance nodeInstance : nodeInstanceList) {
            int elementType = FlowModelUtil.getElementType(nodeInstance.getNodeKey(), flowElementMap);
            if (elementType != FlowElementType.USER_TASK
                && elementType != FlowElementType.END_EVENT
                && elementType != FlowElementType.CALL_ACTIVITY) {
                LOGGER.info("getActiveNodeForRollback: ignore un-userTask or un-endEvent or un-callActivity nodeInstance.||flowInstanceId={}"
                    + "||suspendNodeInstanceId={}||nodeKey={}", flowInstanceId, suspendNodeInstanceId, nodeInstance.getNodeKey());
                continue;
            }

            if (nodeInstance.getStatus() == NodeInstanceStatus.ACTIVE) {
                if (nodeInstance.getNodeInstanceId().equals(suspendNodeInstanceId)) {
                    LOGGER.info("getActiveNodeForRollback: roll back the active Node."
                        + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
                    return nodeInstance;
                }
            } else if (nodeInstance.getStatus() == NodeInstanceStatus.COMPLETED) {
                if (nodeInstance.getNodeInstanceId().equals(suspendNodeInstanceId)) {
                    LOGGER.info("getActiveNodeForRollback: roll back the lasted completed Node."
                            + "||flowInstanceId={}||suspendNodeInstanceId={}||activeNodeInstanceId={}",
                        flowInstanceId, suspendNodeInstanceId, nodeInstance);
                    return nodeInstance;
                }

                LOGGER.warn("getActiveNodeForRollback: cannot rollback the Node."
                    + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
                return null;
            }
            LOGGER.info("getActiveNodeForRollback: ignore disabled Node instance.||flowInstanceId={}"
                + "||suspendNodeInstanceId={}||status={}", flowInstanceId, suspendNodeInstanceId, nodeInstance.getStatus());

        }
        LOGGER.warn("getActiveNodeForRollback: cannot rollback the suspendNodeInstance."
            + "||flowInstanceId={}||suspendNodeInstanceId={}", flowInstanceId, suspendNodeInstanceId);
        return null;
    }

    private void doRollback(RuntimeContext runtimeContext) throws ProcessException {
        AbstractRuntimeExecutor runtimeExecutor = getRollbackExecutor(runtimeContext);
        while (runtimeExecutor != null) {
            runtimeExecutor.rollback(runtimeContext);
            runtimeExecutor = runtimeExecutor.getRollbackExecutor(runtimeContext);
        }
    }

    private void postRollback(RuntimeContext runtimeContext) {

        if (runtimeContext.getProcessStatus() != ProcessStatus.SUCCESS) {
            LOGGER.warn("postRollback: ignore while process failed.||runtimeContext={}", runtimeContext);
            return;
        }
        if (runtimeContext.getCurrentNodeInstance() != null) {
            runtimeContext.setSuspendNodeInstance(runtimeContext.getCurrentNodeInstance());
        }

        //update FlowInstancePO to db
        saveNodeInstanceList(runtimeContext, NodeInstanceType.ROLLBACK);

        if (FlowModelUtil.isElementType(runtimeContext.getCurrentNodeModel().getKey(), runtimeContext.getFlowElementMap(), FlowElementType.START_EVENT)) {
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.TERMINATED);
            flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.TERMINATED);
        } else if (runtimeContext.getFlowInstanceStatus() == FlowInstanceStatus.END) {
            runtimeContext.setFlowInstanceStatus(FlowInstanceStatus.RUNNING);
            flowInstanceService.updateStatus(runtimeContext.getFlowInstanceId(), FlowInstanceStatus.RUNNING);
        }
    }

    private void fillRollbackContext(RuntimeContext runtimeContext, NodeInstance nodeInstance, Map<String, InstanceDataVo> instanceDataMap) throws ProcessException {
        runtimeContext.setInstanceDataId(nodeInstance.getInstanceDataId());
        runtimeContext.setInstanceDataMap(instanceDataMap);
        runtimeContext.setNodeInstanceList(new ArrayList<>());
        NodeInstanceBo suspendNodeInstance = buildSuspendNodeInstance(nodeInstance);
        runtimeContext.setSuspendNodeInstance(suspendNodeInstance);
        setCurrentFlowModel(runtimeContext);
    }

    private NodeInstanceBo buildSuspendNodeInstance(NodeInstance nodeInstance) {
        return PojoUtil.copyBean(nodeInstance, NodeInstanceBo.class);
    }

    private void updateSuspendNodeInstanceBO(NodeInstanceBo suspendNodeInstance, NodeInstance nodeInstance, String instanceDataId) {
        suspendNodeInstance.setId(nodeInstance.getId());
        suspendNodeInstance.setNodeKey(nodeInstance.getNodeKey());
        suspendNodeInstance.setStatus(nodeInstance.getStatus());
        suspendNodeInstance.setSourceNodeInstanceId(nodeInstance.getSourceNodeInstanceId());
        suspendNodeInstance.setSourceNodeKey(nodeInstance.getSourceNodeKey());
        suspendNodeInstance.setInstanceDataId(instanceDataId);
    }

    //suspendNodeInstanceBO is necessary
    private void setCurrentFlowModel(RuntimeContext runtimeContext) throws ProcessException {
        NodeInstanceBo suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        FlowElement currentNodeModel = FlowModelUtil.getFlowElement(runtimeContext.getFlowElementMap(), suspendNodeInstance.getNodeKey());
        if (currentNodeModel == null) {
            LOGGER.warn("setCurrentFlowModel failed: cannot get currentNodeModel.||flowInstance={}||flowDeployId={}||nodeKey={}",
                    runtimeContext.getFlowInstanceId(), runtimeContext.getFlowDeployId(), suspendNodeInstance.getNodeKey());
            throw new ProcessException(ErrorEnum.GET_NODE_FAILED);
        }
        runtimeContext.setCurrentNodeModel(currentNodeModel);
    }

    @Override
    protected boolean isCompleted(RuntimeContext runtimeContext) throws ProcessException {
        if (runtimeContext.getFlowInstanceStatus() == FlowInstanceStatus.COMPLETED) {
            return true;
        }
        if (runtimeContext.getFlowInstanceStatus() == FlowInstanceStatus.END) {
            return false;
        }
        NodeInstanceBo suspendNodeInstance = runtimeContext.getSuspendNodeInstance();
        if (suspendNodeInstance == null) {
            LOGGER.warn("suspendNodeInstance is null.||runtimeContext={}", runtimeContext);
            return false;
        }

        if (suspendNodeInstance.getStatus() == null || suspendNodeInstance.getStatus() != NodeInstanceStatus.COMPLETED) {
            return false;
        }

        String nodeKey = suspendNodeInstance.getNodeKey();
        Map<String, FlowElement> flowElementMap = runtimeContext.getFlowElementMap();
        return FlowModelUtil.getFlowElement(flowElementMap, nodeKey).getType() == FlowElementType.END_EVENT;
    }

    @Override
    protected AbstractRuntimeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws ProcessException {
        return getElementExecutor(runtimeContext);
    }

    @Override
    protected AbstractRuntimeExecutor getRollbackExecutor(RuntimeContext runtimeContext) throws ProcessException {
        return getElementExecutor(runtimeContext);
    }

    private AbstractRuntimeExecutor getElementExecutor(RuntimeContext runtimeContext) throws ProcessException {
        //if process completed, return null
        if (isCompleted(runtimeContext)) {
            return null;
        }
        return executorFactory.getElementExecutor(runtimeContext.getCurrentNodeModel());
    }

    ////////////////////////////////////////common////////////////////////////////////////

    private void saveNodeInstanceList(RuntimeContext runtimeContext, int nodeInstanceType) {
        List<NodeInstanceBo> processNodeList = runtimeContext.getNodeInstanceList();
        if (CollectionUtils.isEmpty(processNodeList)) {
            LOGGER.warn("saveNodeInstanceList: processNodeList is empty,||flowInstanceId={}||nodeInstanceType={}", runtimeContext.getFlowInstanceId(), nodeInstanceType);
            return;
        }
        List<NodeInstance> nodeInstanceList = new ArrayList<>();
        List<NodeInstanceLog> nodeInstanceLogList = new ArrayList<>();

        processNodeList.forEach(nodeInstanceBo -> {
            NodeInstance nodeInstance = buildNodeInstance(runtimeContext, nodeInstanceBo);
            if (nodeInstance != null) {
                nodeInstanceList.add(nodeInstance);
                //build nodeInstance log
                NodeInstanceLog nodeInstanceLog = buildNodeInstanceLog(nodeInstance, nodeInstanceType);
                nodeInstanceLogList.add(nodeInstanceLog);
            }
        });
        nodeInstanceService.insertOrUpdateList(nodeInstanceList);
        nodeInstanceLogService.saveBatch(nodeInstanceLogList);
    }

    private NodeInstance buildNodeInstance(RuntimeContext runtimeContext, NodeInstanceBo nodeInstanceBo) {
        if (runtimeContext.getProcessStatus() == ProcessStatus.FAILED) {
            //set status=FAILED unless it is origin processNodeInstance(suspendNodeInstance)
            if (nodeInstanceBo.getNodeKey().equals(runtimeContext.getSuspendNodeInstance().getNodeKey())) {
                //keep suspendNodeInstance's status while process failed.
                return null;
            }
            nodeInstanceBo.setStatus(NodeInstanceStatus.FAILED);
        }

        NodeInstance nodeInstance = PojoUtil.copyBean(nodeInstanceBo, NodeInstance.class);
        nodeInstance.setFlowInstanceId(runtimeContext.getFlowInstanceId());
        nodeInstance.setFlowDeployId(runtimeContext.getFlowDeployId());
        nodeInstance.setTenant(runtimeContext.getTenant());
        nodeInstance.setCaller(runtimeContext.getCaller());
        return nodeInstance;
    }

    private NodeInstanceLog buildNodeInstanceLog(NodeInstance nodeInstance, int nodeInstanceType) {
        NodeInstanceLog nodeInstanceLog = PojoUtil.copyBean(nodeInstance, NodeInstanceLog.class);
        nodeInstanceLog.setType(nodeInstanceType);
        return nodeInstanceLog;
    }

}
