package cn.sinozg.applet.iot.common.enums;

import lombok.Getter;

/**
 * 注入这些事件后 js里面直接调用对应的方法
 * 获取到数据
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 20:10
 */
@Getter
public enum ScriptEnvType {
    /** 设备行为 */
    BEHAVIOUR ("deviceBehaviour", "设备行为"),
    /** 工具 */
    TOOL ("apiTool", "api工具"),
    /** 组件 */
    COMPONENT ("component", "组件")
    ;
    private final String code;

    private final String name;

    ScriptEnvType(String code, String name){
        this.code = code;
        this.name = name;;
    }
}
