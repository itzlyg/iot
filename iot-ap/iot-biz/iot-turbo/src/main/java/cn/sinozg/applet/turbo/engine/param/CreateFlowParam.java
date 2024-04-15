package cn.sinozg.applet.turbo.engine.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFlowParam extends OperationParam {
    private String flowKey;
    private String flowName;
    private String remark;
    public CreateFlowParam(String tenant, String caller) {
        super(tenant, caller);
    }
}
