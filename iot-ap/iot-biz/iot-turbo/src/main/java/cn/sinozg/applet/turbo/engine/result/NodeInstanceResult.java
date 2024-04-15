package cn.sinozg.applet.turbo.engine.result;

import cn.sinozg.applet.turbo.engine.bo.NodeInstanceMidBo;

public class NodeInstanceResult extends CommonResult {
    private NodeInstanceMidBo nodeInstance;

    public NodeInstanceMidBo getNodeInstance() {
        return nodeInstance;
    }

    public void setNodeInstance(NodeInstanceMidBo nodeInstance) {
        this.nodeInstance = nodeInstance;
    }

}
