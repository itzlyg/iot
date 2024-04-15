package cn.sinozg.applet.turbo.engine.result;

import cn.sinozg.applet.turbo.engine.common.ErrorEnum;
import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;

import java.util.List;

public class InstanceDataListResult extends CommonResult {
    private List<InstanceDataVo> variables;

    public InstanceDataListResult(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public List<InstanceDataVo> getVariables() {
        return variables;
    }

    public void setVariables(List<InstanceDataVo> variables) {
        this.variables = variables;
    }

}
