package cn.sinozg.applet.turbo.engine.param;

import cn.sinozg.applet.turbo.engine.common.RuntimeContext;
import lombok.Data;

@Data
public class RuntimeTaskParam {
    private String flowInstanceId;
    private String taskInstanceId;
    // For internal transmission runtimeContext
    private RuntimeContext runtimeContext;
}
