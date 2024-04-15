package cn.sinozg.applet.turbo.engine.service;

import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import cn.sinozg.applet.turbo.tb.entity.FlowDeployment;
import cn.sinozg.applet.turbo.tb.entity.FlowInstance;
import cn.sinozg.applet.turbo.tb.entity.NodeInstance;
import cn.sinozg.applet.turbo.tb.service.FlowDeploymentService;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceService;
import cn.sinozg.applet.turbo.tb.service.NodeInstanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class NodeInstanceDataService {

    @Resource
    private NodeInstanceService nodeInstanceService;

    @Resource
    private FlowInstanceService flowInstanceService;

    @Resource
    protected FlowDeploymentService flowDeploymentService;
    @Resource
    private FlowInstanceDataService flowInstanceDataService;

    public NodeInstance selectByNodeInstanceId(String flowInstanceId, String nodeInstanceId, boolean effectiveForSubFlowInstance) {
        NodeInstance nodeInstance = nodeInstanceService.selectByNodeInstanceId(flowInstanceId, nodeInstanceId);
        if (!effectiveForSubFlowInstance) {
            return nodeInstance;
        }
        if (nodeInstance != null) {
            return nodeInstance;
        }
        String subFlowInstanceId = flowInstanceDataService.getFlowInstanceIdByRootFlowInstanceIdAndNodeInstanceId(flowInstanceId, nodeInstanceId);
        return nodeInstanceService.selectByNodeInstanceId(subFlowInstanceId, nodeInstanceId);
    }

    public NodeInstance selectRecentEndNode(String flowInstanceId) {
        FlowInstance rootFlowInstance = flowInstanceService.selectByFlowInstanceId(flowInstanceId);
        FlowDeployment rootFlowDeployment = flowDeploymentService.selectByDeployId(rootFlowInstance.getFlowDeployId());
        Map<String, FlowElement> rootFlowElementMap = FlowModelUtil.getFlowElementMap(rootFlowDeployment.getFlowModel());

        List<NodeInstance> nodeInstanceList = nodeInstanceService.selectDescByFlowInstanceId(flowInstanceId);
        for (NodeInstance nodeInstance : nodeInstanceList) {
            int elementType = FlowModelUtil.getElementType(nodeInstance.getNodeKey(), rootFlowElementMap);
            if (elementType == FlowElementType.END_EVENT) {
                return nodeInstance;
            }
        }
        return null;
    }
}
