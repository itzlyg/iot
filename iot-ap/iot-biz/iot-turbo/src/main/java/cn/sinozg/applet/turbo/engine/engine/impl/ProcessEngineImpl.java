package cn.sinozg.applet.turbo.engine.engine.impl;

import cn.sinozg.applet.turbo.engine.engine.ProcessEngine;
import cn.sinozg.applet.turbo.engine.param.CommitTaskParam;
import cn.sinozg.applet.turbo.engine.param.CreateFlowParam;
import cn.sinozg.applet.turbo.engine.param.DeployFlowParam;
import cn.sinozg.applet.turbo.engine.param.GetFlowModuleParam;
import cn.sinozg.applet.turbo.engine.param.RollbackTaskParam;
import cn.sinozg.applet.turbo.engine.param.StartProcessParam;
import cn.sinozg.applet.turbo.engine.param.UpdateFlowParam;
import cn.sinozg.applet.turbo.engine.processor.DefinitionProcessor;
import cn.sinozg.applet.turbo.engine.processor.RuntimeProcessor;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProcessEngineImpl implements ProcessEngine {

    @Resource
    private DefinitionProcessor definitionProcessor;

    @Resource
    private RuntimeProcessor runtimeProcessor;

    @Override
    public CreateFlowResult createFlow(CreateFlowParam createFlowParam) {
        return definitionProcessor.create(createFlowParam);
    }

    @Override
    public UpdateFlowResult updateFlow(UpdateFlowParam updateFlowParam) {
        return definitionProcessor.update(updateFlowParam);
    }

    @Override
    public DeployFlowResult deployFlow(DeployFlowParam deployFlowParam) {
        return definitionProcessor.deploy(deployFlowParam);
    }

    @Override
    public FlowModuleResult getFlowModule(GetFlowModuleParam getFlowModuleParam) {
        return definitionProcessor.getFlowModule(getFlowModuleParam);
    }

    @Override
    public StartProcessResult startProcess(StartProcessParam startProcessParam) {
        return runtimeProcessor.startProcess(startProcessParam);
    }

    @Override
    public CommitTaskResult commitTask(CommitTaskParam commitTaskParam) {
        runtimeProcessor.checkIsSubFlowInstance(commitTaskParam.getFlowInstanceId());
        return runtimeProcessor.commit(commitTaskParam);
    }

    @Override
    public RollbackTaskResult rollbackTask(RollbackTaskParam rollbackTaskParam) {
        runtimeProcessor.checkIsSubFlowInstance(rollbackTaskParam.getFlowInstanceId());
        return runtimeProcessor.rollback(rollbackTaskParam);
    }

    @Override
    public TerminateResult terminateProcess(String flowInstanceId) {
        runtimeProcessor.checkIsSubFlowInstance(flowInstanceId);
        return runtimeProcessor.terminateProcess(flowInstanceId, true);
    }

    @Override
    public TerminateResult terminateProcess(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        runtimeProcessor.checkIsSubFlowInstance(flowInstanceId);
        return runtimeProcessor.terminateProcess(flowInstanceId, effectiveForSubFlowInstance);
    }

    @Override
    public NodeInstanceListResult getHistoryUserTaskList(String flowInstanceId) {
        return runtimeProcessor.getHistoryUserTaskList(flowInstanceId, true);
    }

    @Override
    public NodeInstanceListResult getHistoryUserTaskList(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        return runtimeProcessor.getHistoryUserTaskList(flowInstanceId, effectiveForSubFlowInstance);
    }

    @Override
    public ElementInstanceListResult getHistoryElementList(String flowInstanceId) {
        return runtimeProcessor.getHistoryElementList(flowInstanceId, true);
    }

    @Override
    public ElementInstanceListResult getHistoryElementList(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        return runtimeProcessor.getHistoryElementList(flowInstanceId, effectiveForSubFlowInstance);
    }

    @Override
    public InstanceDataListResult getInstanceData(String flowInstanceId) {
        return runtimeProcessor.getInstanceData(flowInstanceId, true);
    }

    @Override
    public InstanceDataListResult getInstanceData(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        return runtimeProcessor.getInstanceData(flowInstanceId, effectiveForSubFlowInstance);
    }

    @Override
    public NodeInstanceResult getNodeInstance(String flowInstanceId, String nodeInstanceId) {
        return runtimeProcessor.getNodeInstance(flowInstanceId, nodeInstanceId, true);
    }

    @Override
    public NodeInstanceResult getNodeInstance(String flowInstanceId, String nodeInstanceId, boolean effectiveForSubFlowInstance) {
        return runtimeProcessor.getNodeInstance(flowInstanceId, nodeInstanceId, effectiveForSubFlowInstance);
    }

    @Override
    public FlowInstanceResult getFlowInstance(String flowInstanceId) {
        return runtimeProcessor.getFlowInstance(flowInstanceId);
    }

    @Override
    public InstanceDataListResult getInstanceData(String flowInstanceId, String instanceDataId) {
        return runtimeProcessor.getInstanceData(flowInstanceId, instanceDataId, true);
    }

    @Override
    public InstanceDataListResult getInstanceData(String flowInstanceId, String instanceDataId, boolean effectiveForSubFlowInstance) {
        return runtimeProcessor.getInstanceData(flowInstanceId, instanceDataId, effectiveForSubFlowInstance);
    }
}
