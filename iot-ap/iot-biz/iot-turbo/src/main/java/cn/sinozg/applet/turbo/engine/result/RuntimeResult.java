package cn.sinozg.applet.turbo.engine.result;

import cn.sinozg.applet.turbo.engine.bo.NodeInstanceMidBo;
import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;

import java.util.List;

public class RuntimeResult extends CommonResult {
    private String flowInstanceId;
    private int status;
    private NodeInstanceMidBo activeTaskInstance;
    private List<InstanceDataVo> variables;

    public RuntimeResult() {
        super();
    }

    public RuntimeResult(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public String getFlowInstanceId() {
        return flowInstanceId;
    }

    public void setFlowInstanceId(String flowInstanceId) {
        this.flowInstanceId = flowInstanceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public NodeInstanceMidBo getActiveTaskInstance() {
        return activeTaskInstance;
    }

    public void setActiveTaskInstance(NodeInstanceMidBo activeTaskInstance) {
        this.activeTaskInstance = activeTaskInstance;
    }

    public List<InstanceDataVo> getVariables() {
        return variables;
    }

    public void setVariables(List<InstanceDataVo> variables) {
        this.variables = variables;
    }

}
