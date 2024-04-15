package cn.sinozg.applet.turbo.engine.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowInstanceBo {
    private String flowInstanceId;
    private String flowDeployId;
    private String flowModuleId;
    private Integer status;
    private String parentFlowInstanceId;
}
