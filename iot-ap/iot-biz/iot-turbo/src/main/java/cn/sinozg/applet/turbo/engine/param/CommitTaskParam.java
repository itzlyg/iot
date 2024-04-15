package cn.sinozg.applet.turbo.engine.param;

import cn.sinozg.applet.turbo.engine.model.InstanceDataVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommitTaskParam extends RuntimeTaskParam {
    private List<InstanceDataVo> variables;
    /** Used to specify the FlowModuleId when commit CallActivity node */
    private String callActivityFlowModuleId;
}
