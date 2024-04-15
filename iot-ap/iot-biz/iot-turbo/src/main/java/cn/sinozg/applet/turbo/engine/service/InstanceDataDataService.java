package cn.sinozg.applet.turbo.engine.service;

import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import cn.sinozg.applet.turbo.tb.entity.FlowDeployment;
import cn.sinozg.applet.turbo.tb.entity.FlowInstance;
import cn.sinozg.applet.turbo.tb.entity.FlowInstanceMapping;
import cn.sinozg.applet.turbo.tb.entity.InstanceData;
import cn.sinozg.applet.turbo.tb.entity.NodeInstance;
import cn.sinozg.applet.turbo.tb.service.FlowDeploymentService;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceMappingService;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceService;
import cn.sinozg.applet.turbo.tb.service.InstanceDataService;
import cn.sinozg.applet.turbo.tb.service.NodeInstanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class InstanceDataDataService {

    @Resource
    private InstanceDataService instanceDataService;

    @Resource
    private FlowInstanceService flowInstanceService;

    @Resource
    protected FlowDeploymentService flowDeploymentService;

    @Resource
    private NodeInstanceService nodeInstanceService;

    @Resource
    private FlowInstanceMappingService flowInstanceMappingService;

    @Resource
    private FlowInstanceDataService flowInstanceDataService;

    public InstanceData select(String flowInstanceId, String instanceDataId, boolean effectiveForSubFlowInstance) {
        InstanceData instanceData = instanceDataService.selectByIns(flowInstanceId, instanceDataId);
        if (!effectiveForSubFlowInstance) {
            return instanceData;
        }
        if (instanceData != null) {
            return instanceData;
        }
        String subFlowInstanceId = flowInstanceDataService.getFlowInstanceIdByRootFlowInstanceIdAndInstanceDataId(flowInstanceId, instanceDataId);
        return instanceDataService.selectByIns(subFlowInstanceId, instanceDataId);
    }

    public InstanceData select(String flowInstanceId, boolean effectiveForSubFlowInstance) {
        InstanceData instanceData = instanceDataService.selectRecent(flowInstanceId);
        if (!effectiveForSubFlowInstance) {
            return instanceData;
        }
        FlowInstance flowInstance = flowInstanceService.selectByFlowInstanceId(flowInstanceId);
        FlowDeployment flowDeployment = flowDeploymentService.selectByDeployId(flowInstance.getFlowDeployId());
        Map<String, FlowElement> flowElementMap = FlowModelUtil.getFlowElementMap(flowDeployment.getFlowModel());

        NodeInstance nodeInstance = nodeInstanceService.selectRecentOne(flowInstanceId);
        int elementType = FlowModelUtil.getElementType(nodeInstance.getNodeKey(), flowElementMap);
        if (elementType != FlowElementType.CALL_ACTIVITY) {
            return instanceDataService.selectRecent(flowInstanceId);
        } else {
            FlowInstanceMapping flowInstanceMapping = flowInstanceMappingService.selectFlowInstanceMapping(flowInstanceId, nodeInstance.getNodeInstanceId());
            return select(flowInstanceMapping.getSubFlowInstanceId(), true);
        }
    }
}
