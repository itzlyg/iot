package cn.sinozg.applet.turbo.engine.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFlowParam extends OperationParam {
    private String flowKey;
    private String flowName;
    private String flowModuleId;
    private String flowModel;
    private String remark;

    public UpdateFlowParam(String tenant, String caller) {
        super(tenant, caller);
    }
}
