package cn.sinozg.applet.turbo.engine.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowBasicInfo {
    private String flowDeployId;
    private String flowModuleId;
    private String tenant;
    private String caller;
}
