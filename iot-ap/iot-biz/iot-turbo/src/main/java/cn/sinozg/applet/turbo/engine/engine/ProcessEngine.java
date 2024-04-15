package cn.sinozg.applet.turbo.engine.engine;


import cn.sinozg.applet.turbo.engine.bo.NodeInstanceMidBo;
import cn.sinozg.applet.turbo.engine.model.FlowModel;
import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;
import cn.sinozg.applet.turbo.engine.model.UserTask;
import cn.sinozg.applet.turbo.engine.param.CommitTaskParam;
import cn.sinozg.applet.turbo.engine.param.CreateFlowParam;
import cn.sinozg.applet.turbo.engine.param.DeployFlowParam;
import cn.sinozg.applet.turbo.engine.param.GetFlowModuleParam;
import cn.sinozg.applet.turbo.engine.param.RollbackTaskParam;
import cn.sinozg.applet.turbo.engine.param.StartProcessParam;
import cn.sinozg.applet.turbo.engine.param.UpdateFlowParam;
import cn.sinozg.applet.turbo.engine.result.CommitTaskResult;
import cn.sinozg.applet.turbo.engine.result.CreateFlowResult;
import cn.sinozg.applet.turbo.engine.result.DeployFlowResult;
import cn.sinozg.applet.turbo.engine.result.ElementInstanceListResult;
import cn.sinozg.applet.turbo.engine.result.FlowInstanceResult;
import cn.sinozg.applet.turbo.engine.result.FlowModuleResult;
import cn.sinozg.applet.turbo.engine.result.InstanceDataListResult;
import cn.sinozg.applet.turbo.engine.result.NodeInstanceListResult;
import cn.sinozg.applet.turbo.engine.result.NodeInstanceResult;
import cn.sinozg.applet.turbo.engine.result.RollbackTaskResult;
import cn.sinozg.applet.turbo.engine.result.StartProcessResult;
import cn.sinozg.applet.turbo.engine.result.TerminateResult;
import cn.sinozg.applet.turbo.engine.result.UpdateFlowResult;
import cn.sinozg.applet.turbo.tb.entity.FlowDefinition;
import cn.sinozg.applet.turbo.tb.entity.FlowDeployment;
import cn.sinozg.applet.turbo.tb.entity.FlowInstance;

/**
 * The entrance of Turbo
 * <p>
 * It mainly provides abilities to:
 * 1.Describe and deploy a process called flow;
 * 2.Process and drive a deployed flow.
 * <p>
 */
public interface ProcessEngine {

    /**
     * Create a flow({@link FlowDefinition}) with flowKey and descriptive info.
     * Attention: The {@link FlowModel} of the flow is empty.
     *
     * @param createFlowParam flowKey: business key for the flow
     *                        flowName/operator/remark: describe the flow
     * @return {@link CreateFlowParam} mainly includes flowModuleId to indicate an unique flow.
     */
    CreateFlowResult createFlow(CreateFlowParam createFlowParam);

    /**
     * Update a flow by flowModuleId. Set/update flowModel or update descriptive info.
     *
     * @param updateFlowParam flowModuleId: specify the flow to update
     *                        flowKey/flowName/flowModel/remark: content to update
     */
    UpdateFlowResult updateFlow(UpdateFlowParam updateFlowParam);

    /**
     * Deploy a flow by flowModuleId.
     * <p>
     * Create a {@link FlowDeployment} every time.
     * A flow can be started to process only after deployed.
     *
     * @param deployFlowParam flowModuleId: specify the flow to deploy
     * @return {@link DeployFlowResult} mainly contains flowDeployId to indicate an unique record of the deployment.
     */
    DeployFlowResult deployFlow(DeployFlowParam deployFlowParam);

    /**
     * Get flow info includes flowModel content, status and descriptive info.
     * <p>
     * It'll query by flowDeployId while the flowDeployId is not blank. Otherwise, it'll query by flowModuleId.
     *
     * @param getFlowModuleParam flowModuleId specify the flow and get info from {@link FlowDefinition}
     *                           flowDeployId specify the flow and get info from {@link FlowDeployment}
     */
    FlowModuleResult getFlowModule(GetFlowModuleParam getFlowModuleParam);

    /**
     * Start process
     * <p>
     * 1.Create a flow instance({@link FlowInstance}) according to the specified
     * flow for the execution every time;
     * 2.Process the flow instance from the unique {@link StartEvent} node
     * until it reaches an {@link UserTask} node or
     * an {@link EndEvent} node.
     *
     * <p>
     * Effective for SubFlowInstance by default
     *
     * @param startProcessParam flowDeployId / flowModuleId: specify the flow to process
     *                          variables: input data to drive the process if required
     * @return {@link StartProcessResult} mainly contains flowInstanceId and activeTaskInstance({@link NodeInstanceMidBo})
     * to describe the userTask to be committed or the EndEvent node instance.
     */
    StartProcessResult startProcess(StartProcessParam startProcessParam);

    /**
     * Commit suspended userTask of the flow instance previously created specified by flowInstanceId and continue to process.
     *
     * <p>
     * Effective for SubFlowInstance by default
     *
     * @param commitTaskParam flowInstanceId: specify the flowInstance of the task
     *                        nodeInstanceId: specify the task to commit
     *                        variables: input data to drive the process if required
     * @return {@link CommitTaskResult} similar to {@link #startProcess(StartProcessParam)}
     */
    CommitTaskResult commitTask(CommitTaskParam commitTaskParam);

    /**
     * Rollback task
     * <p>
     * According to the historical node instance list, it'll rollback the suspended userTask of the flow instance
     * specified by flowInstanceId forward until it reaches an UserTask node or an StartEvent node.
     *
     * <p>
     * Effective for SubFlowInstance by default
     *
     * @param rollbackTaskParam flowInstanceId / nodeInstanceId similar to {@link #commitTask(CommitTaskParam)}
     * @return {@link RollbackTaskResult} similar to {@link #commitTask(CommitTaskParam)}
     */
    RollbackTaskResult rollbackTask(RollbackTaskParam rollbackTaskParam);

    /**
     * Terminate process
     * <p>
     * If the specified flow instance has been completed, ignore. Otherwise, set status to terminated of the flow instance.
     *
     * <p>
     * Effective for SubFlowInstance by default
     *
     * @param flowInstanceId
     * @return {@link TerminateResult} similar to {@link #commitTask(CommitTaskParam)} without activeTaskInstance.
     */
    TerminateResult terminateProcess(String flowInstanceId);

    /**
     * Terminate process
     * <p>
     * If the specified flow instance has been completed, ignore. Otherwise, set status to terminated of the flow instance.
     *
     * @param flowInstanceId
     * @param effectiveForSubFlowInstance
     * @return {@link TerminateResult} similar to {@link #commitTask(CommitTaskParam)} without activeTaskInstance.
     */
    TerminateResult terminateProcess(String flowInstanceId, boolean effectiveForSubFlowInstance);

    /**
     * Get historical UserTask list
     * <p>
     * Get the list of processed UserTask of the specified flow instance order by processed time desc.
     * Attention: it'll include active userTask(s) and completed userTask(s) in the list without disabled userTask(s).
     *
     * <p>
     * Effective for SubFlowInstance by default
     *
     * @param flowInstanceId
     */
    NodeInstanceListResult getHistoryUserTaskList(String flowInstanceId);

    /**
     * Get historical UserTask list
     * <p>
     * Get the list of processed UserTask of the specified flow instance order by processed time desc.
     * Attention: it'll include active userTask(s) and completed userTask(s) in the list without disabled userTask(s).
     *
     * @param flowInstanceId
     * @param effectiveForSubFlowInstance
     */
    NodeInstanceListResult getHistoryUserTaskList(String flowInstanceId, boolean effectiveForSubFlowInstance);

    /**
     * Get processed element instance list for the specified flow instance, and mainly used to show the view of the snapshot.
     *
     * <p>
     * Effective for SubFlowInstance by default
     *
     * @param flowInstanceId flowInstance ID
     * @return {@link ElementInstanceListResult} the list of nodes executed in history
     */
    ElementInstanceListResult getHistoryElementList(String flowInstanceId);

    /**
     * Get processed element instance list for the specified flow instance, and mainly used to show the view of the snapshot.
     *
     * @param flowInstanceId              flowInstance ID
     * @param effectiveForSubFlowInstance
     * @return {@link ElementInstanceListResult} the list of nodes executed in history
     */
    ElementInstanceListResult getHistoryElementList(String flowInstanceId, boolean effectiveForSubFlowInstance);

    /**
     * Get latest {@link InstanceDataVo} list of the specified flow instance.
     *
     * <p>
     * Effective for SubFlowInstance by default
     *
     * @param flowInstanceId
     */
    InstanceDataListResult getInstanceData(String flowInstanceId);

    /**
     * Get latest {@link InstanceDataVo} list of the specified flow instance.
     *
     * @param flowInstanceId
     * @param effectiveForSubFlowInstance
     */
    InstanceDataListResult getInstanceData(String flowInstanceId, boolean effectiveForSubFlowInstance);

    /**
     * Get {@link InstanceDataVo} list of the specified instance data.
     *
     * <p>
     * Effective for SubFlowInstance by default
     *
     * @param flowInstanceId
     * @param instanceDataId
     */
    InstanceDataListResult getInstanceData(String flowInstanceId, String instanceDataId);

    /**
     * Get {@link InstanceDataVo} list of the specified instance data.
     *
     * @param flowInstanceId
     * @param instanceDataId
     * @param effectiveForSubFlowInstance
     */
    InstanceDataListResult getInstanceData(String flowInstanceId, String instanceDataId, boolean effectiveForSubFlowInstance);

    /**
     * According to the flow instance and node instance given in, get node instance info.
     *
     * <p>
     * Effective for SubFlowInstance by default
     *
     * @param flowInstanceId
     * @param nodeInstanceId
     */
    NodeInstanceResult getNodeInstance(String flowInstanceId, String nodeInstanceId);

    /**
     * According to the flow instance and node instance given in, get node instance info.
     *
     * @param flowInstanceId
     * @param nodeInstanceId
     * @param effectiveForSubFlowInstance
     */
    NodeInstanceResult getNodeInstance(String flowInstanceId, String nodeInstanceId, boolean effectiveForSubFlowInstance);

    /**
     * According to the flow instance given in, get flow instance info.
     *
     * @param flowInstanceId
     */
    FlowInstanceResult getFlowInstance(String flowInstanceId);
}
