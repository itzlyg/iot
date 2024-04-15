package cn.sinozg.applet.turbo.engine.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeployFlowParam extends OperationParam {
    private String flowModuleId;
    public DeployFlowParam(String tenant, String caller) {
        super(tenant, caller);
    }

}
