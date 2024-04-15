package cn.sinozg.applet.turbo.engine.service;

import cn.sinozg.applet.turbo.engine.common.FlowElementType;
import cn.sinozg.applet.turbo.engine.model.FlowElement;
import cn.sinozg.applet.turbo.engine.util.FlowModelUtil;
import cn.sinozg.applet.turbo.tb.entity.FlowDeployment;
import cn.sinozg.applet.turbo.tb.entity.FlowInstance;
import cn.sinozg.applet.turbo.tb.entity.FlowInstanceMapping;
import cn.sinozg.applet.turbo.tb.entity.NodeInstance;
import cn.sinozg.applet.turbo.tb.service.FlowDeploymentService;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceMappingService;
import cn.sinozg.applet.turbo.tb.service.FlowInstanceService;
import cn.sinozg.applet.turbo.tb.service.NodeInstanceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

@Service
public class FlowInstanceDataService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(FlowInstanceDataService.class);

    @Resource
    private NodeInstanceService nodeInstanceService;

    @Resource
    private FlowInstanceMappingService flowInstanceMappingService;

    @Resource
    private FlowInstanceService flowInstanceService;

    @Resource
    protected FlowDeploymentService flowDeploymentService;

    /**
     * According to rootFlowInstanceId and commitNodeInstanceId, build and return NodeInstance stack.
     * When the subProcessInstance of each layer is executed, stack needs to pop up.
     * <p>
     * e.g.
     * <p>
     * rootNodeInstanceId
     * ^
     * ..................
     * ^
     * commitNodeInstanceId
     *
     * @param rootFlowInstanceId
     * @param commitNodeInstanceId
     * @return
     */
    public Stack<String> getNodeInstanceIdStack(String rootFlowInstanceId, String commitNodeInstanceId) {
        if (StringUtils.isBlank(commitNodeInstanceId)) {
            LOGGER.info("getNodeInstanceId2RootStack result is empty.||rootFlowInstanceId={}||commitNodeInstanceId={}", rootFlowInstanceId, commitNodeInstanceId);
            return new Stack<>();
        }
        FlowInstanceTreeResult flowInstanceTreeResult = buildFlowInstanceTree(rootFlowInstanceId, commitNodeInstanceId);
        NodeInstancePojo rightNodeInstance = flowInstanceTreeResult.getInterruptNodeInstancePojo();
        Stack<String> stack = new Stack<>();
        while (rightNodeInstance != null) {
            stack.push(rightNodeInstance.getId());
            rightNodeInstance = rightNodeInstance.getFlowInstance().getBelongNodeInstance();
        }
        LOGGER.info("getNodeInstanceId2RootStack result.||rootFlowInstanceId={}||commitNodeInstanceId={}||result={}", rootFlowInstanceId, commitNodeInstanceId, stack);
        return stack;
    }

    /**
     * According to rootFlowInstanceId, get all subFlowInstanceIds from db.
     *
     * @param rootFlowInstanceId
     * @return
     */
    public Set<String> getAllSubFlowInstanceIds(String rootFlowInstanceId) {
        FlowInstanceTreeResult flowInstanceTreeResult = buildFlowInstanceTreeCommon(rootFlowInstanceId, null);
        FlowInstancePojo flowInstancePojo = flowInstanceTreeResult.getRootFlowInstancePojo();
        Set<String> result = getAllSubFlowInstanceIdsInternal(flowInstancePojo);
        result.remove(rootFlowInstanceId);
        LOGGER.info("getAllSubFlowInstanceIds result.||rootFlowInstanceId={}||result={}", rootFlowInstanceId, result);
        return result;
    }

    private Set<String> getAllSubFlowInstanceIdsInternal(FlowInstancePojo flowInstancePojo) {
        Set<String> result = new TreeSet<>();
        if (flowInstancePojo == null) {
            return result;
        }
        result.add(flowInstancePojo.getId());
        List<NodeInstancePojo> nodeInstanceList = flowInstancePojo.getNodeInstanceList();
        for (NodeInstancePojo nodeInstancePojo : nodeInstanceList) {
            if (CollectionUtils.isEmpty(nodeInstancePojo.getSubFlowInstanceList())) {
                continue;
            }
            FlowInstancePojo subFlowInstancePojo = nodeInstancePojo.getSubFlowInstanceList().get(0);
            Set<String> subFlowInstanceResult = getAllSubFlowInstanceIdsInternal(subFlowInstancePojo);
            result.addAll(subFlowInstanceResult);
        }
        return result;
    }


    /**
     * According to rootFlowInstanceId and nodeInstanceId,
     * Return the FlowInstanceId where the nodeInstanceId is located.
     *
     * @param rootFlowInstanceId
     * @param nodeInstanceId
     * @return
     */
    public String getFlowInstanceIdByRootFlowInstanceIdAndNodeInstanceId(String rootFlowInstanceId, String nodeInstanceId) {
        if (StringUtils.isBlank(nodeInstanceId)) {
            return StringUtils.EMPTY;
        }
        FlowInstanceTreeResult flowInstanceTreeResult = buildFlowInstanceTree(rootFlowInstanceId, nodeInstanceId);
        NodeInstancePojo rightNodeInstance = flowInstanceTreeResult.getInterruptNodeInstancePojo();
        if (rightNodeInstance == null) {
            return StringUtils.EMPTY;
        }
        return rightNodeInstance.getFlowInstance().getId();
    }

    /**
     * According to rootFlowInstanceId and instanceDataId,
     * Return the FlowInstanceId where the instanceDataId is located.
     *
     * @param rootFlowInstanceId
     * @param instanceDataId
     * @return
     */
    public String getFlowInstanceIdByRootFlowInstanceIdAndInstanceDataId(String rootFlowInstanceId, String instanceDataId) {
        if (StringUtils.isBlank(instanceDataId)) {
            return StringUtils.EMPTY;
        }
        FlowInstanceTreeResult flowInstanceTreeResult = buildFlowInstanceTree(rootFlowInstanceId, instanceDataId);
        NodeInstancePojo rightNodeInstance = flowInstanceTreeResult.getInterruptNodeInstancePojo();
        if (rightNodeInstance == null) {
            return StringUtils.EMPTY;
        }
        return rightNodeInstance.getFlowInstance().getId();
    }

    private FlowInstanceTreeResult buildFlowInstanceTree(String rootFlowInstanceId, String instanceDataId){
        return buildFlowInstanceTreeCommon(rootFlowInstanceId, nodeInstancePo -> {
            if (nodeInstancePo != null) {
                return nodeInstancePo.getInstanceDataId().equals(instanceDataId);
            }
            return false;
        });
    }

    // common : build a flowInstanceAndNodeInstance tree
    private FlowInstanceTreeResult buildFlowInstanceTreeCommon(String rootFlowInstanceId, InterruptCondition interruptCondition) {
        FlowInstanceTreeResult flowInstanceTreeResult = new FlowInstanceTreeResult();
        FlowInstancePojo flowInstance = new FlowInstancePojo();
        flowInstance.setId(rootFlowInstanceId);
        flowInstanceTreeResult.setRootFlowInstancePojo(flowInstance);

        FlowInstance rootFlowInstance = flowInstanceService.selectByFlowInstanceId(rootFlowInstanceId);
        FlowDeployment rootFlowDeployment = flowDeploymentService.selectByDeployId(rootFlowInstance.getFlowDeployId());
        Map<String, FlowElement> rootFlowElementMap = FlowModelUtil.getFlowElementMap(rootFlowDeployment.getFlowModel());

        List<NodeInstance> nodeInstanceList = nodeInstanceService.selectDescByFlowInstanceId(rootFlowInstanceId);
        for (NodeInstance nodeInstance : nodeInstanceList) {
            NodeInstancePojo ni = new NodeInstancePojo();
            ni.setId(nodeInstance.getNodeInstanceId());
            ni.setFlowInstance(flowInstance);
            flowInstance.getNodeInstanceList().add(ni);

            if (interruptCondition != null && interruptCondition.match(nodeInstance)) {
                flowInstanceTreeResult.setInterruptNodeInstancePojo(ni);
                return flowInstanceTreeResult;
            }

            int elementType = FlowModelUtil.getElementType(nodeInstance.getNodeKey(), rootFlowElementMap);
            if (elementType != FlowElementType.CALL_ACTIVITY) {
                continue;
            }
            List<FlowInstanceMapping> flowInstanceMappingList = flowInstanceMappingService.selectFlowInstanceMappingList(nodeInstance.getFlowInstanceId(), nodeInstance.getNodeInstanceId());
            for (FlowInstanceMapping flowInstanceMapping : flowInstanceMappingList) {
                FlowInstanceTreeResult subFlowInstanceTreeResult = buildFlowInstanceTreeCommon(flowInstanceMapping.getSubFlowInstanceId(), interruptCondition);
                FlowInstancePojo subFlowInstance = subFlowInstanceTreeResult.getRootFlowInstancePojo();
                subFlowInstance.setBelongNodeInstance(ni);
                ni.getSubFlowInstanceList().add(subFlowInstance);
                if (subFlowInstanceTreeResult.needInterrupt()) {
                    flowInstanceTreeResult.setInterruptNodeInstancePojo(subFlowInstanceTreeResult.getInterruptNodeInstancePojo());
                    return flowInstanceTreeResult;
                }
            }
        }
        return flowInstanceTreeResult;
    }

    private static class FlowInstancePojo {
        private String id;
        private NodeInstancePojo belongNodeInstance;
        private List<NodeInstancePojo> nodeInstanceList = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public NodeInstancePojo getBelongNodeInstance() {
            return belongNodeInstance;
        }

        public void setBelongNodeInstance(NodeInstancePojo belongNodeInstance) {
            this.belongNodeInstance = belongNodeInstance;
        }

        public List<NodeInstancePojo> getNodeInstanceList() {
            return nodeInstanceList;
        }

        public void setNodeInstanceList(List<NodeInstancePojo> nodeInstanceList) {
            this.nodeInstanceList = nodeInstanceList;
        }
    }

    private static class NodeInstancePojo {

        private String id;
        private FlowInstancePojo flowInstance;
        private List<FlowInstancePojo> subFlowInstanceList = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public FlowInstancePojo getFlowInstance() {
            return flowInstance;
        }

        public void setFlowInstance(FlowInstancePojo flowInstance) {
            this.flowInstance = flowInstance;
        }

        public List<FlowInstancePojo> getSubFlowInstanceList() {
            return subFlowInstanceList;
        }

        public void setSubFlowInstanceList(List<FlowInstancePojo> subFlowInstanceList) {
            this.subFlowInstanceList = subFlowInstanceList;
        }
    }

    private static class FlowInstanceTreeResult {
        private FlowInstancePojo rootFlowInstancePojo;
        private NodeInstancePojo interruptNodeInstancePojo;

        public FlowInstancePojo getRootFlowInstancePojo() {
            return rootFlowInstancePojo;
        }

        public void setRootFlowInstancePojo(FlowInstancePojo rootFlowInstancePojo) {
            this.rootFlowInstancePojo = rootFlowInstancePojo;
        }

        public NodeInstancePojo getInterruptNodeInstancePojo() {
            return interruptNodeInstancePojo;
        }

        public void setInterruptNodeInstancePojo(NodeInstancePojo interruptNodeInstancePojo) {
            this.interruptNodeInstancePojo = interruptNodeInstancePojo;
        }

        public boolean needInterrupt() {
            return interruptNodeInstancePojo != null;
        }
    }

    /**
     * When build a flowInstanceAndNodeInstance tree,
     * we allow timely interruption to improve response.
     */
    private interface InterruptCondition {

        /**
         * Returns true when the condition is match
         *
         * @param nodeInstance
         * @return
         */
        boolean match(NodeInstance nodeInstance);
    }
}
