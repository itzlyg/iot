package cn.sinozg.applet.turbo.engine.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationParam extends CommonParam {

    private String operator;
    public OperationParam(){
        super(null, null);

    }
    public OperationParam(String tenant, String caller) {
        super(tenant, caller);
    }
}
