package cn.sinozg.applet.turbo.engine.result;

import cn.sinozg.applet.turbo.engine.bo.NodeInstanceMidBo;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;

import java.util.List;

public class NodeInstanceListResult extends CommonResult {
    private List<NodeInstanceMidBo> nodeInstanceList;

    public NodeInstanceListResult(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public List<NodeInstanceMidBo> getNodeInstanceList() {
        return nodeInstanceList;
    }

    public void setNodeInstanceList(List<NodeInstanceMidBo> nodeInstanceList) {
        this.nodeInstanceList = nodeInstanceList;
    }
}
