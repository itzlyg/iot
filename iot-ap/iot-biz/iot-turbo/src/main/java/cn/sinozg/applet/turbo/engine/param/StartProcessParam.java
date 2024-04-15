package cn.sinozg.applet.turbo.engine.param;

import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StartProcessParam {
    /** For internal transmission runtimeContext */
    private RuntimeContext runtimeContext;

    private String flowModuleId;
    private String flowDeployId;
    private List<InstanceDataVo> variables;
}
