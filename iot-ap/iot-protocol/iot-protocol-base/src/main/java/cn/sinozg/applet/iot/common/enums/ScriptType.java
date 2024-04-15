package cn.sinozg.applet.iot.common.enums;

import cn.sinozg.applet.iot.script.service.DataAnalysisService;
import cn.sinozg.applet.iot.script.service.ScriptEngineService;
import cn.sinozg.applet.iot.script.service.impl.JavaDataAnalysisServiceImpl;
import cn.sinozg.applet.iot.script.service.impl.JavaScriptEngineServiceImpl;
import cn.sinozg.applet.iot.script.service.impl.JsDataAnalysisServiceImpl;
import cn.sinozg.applet.iot.script.service.impl.JsScriptEngineServiceImpl;
import cn.sinozg.applet.iot.script.service.impl.LuaDataAnalysisServiceImpl;
import cn.sinozg.applet.iot.script.service.impl.LuaScriptEnginServiceImpl;
import lombok.Getter;

/**
 * 脚本类型枚举值
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 17:30
 */
@Getter
public enum ScriptType {

    /** js脚本 */
    JS("01", "JavaScript", JsScriptEngineServiceImpl.class, JsDataAnalysisServiceImpl.class),
    /** lua */
    LUA("02", "lua", LuaScriptEnginServiceImpl.class, LuaDataAnalysisServiceImpl.class),
    /** py */
    PY("03", "python", JsScriptEngineServiceImpl.class, JsDataAnalysisServiceImpl.class),
    /** java */
    JAVA("04", "java", JavaScriptEngineServiceImpl.class, JavaDataAnalysisServiceImpl.class),
    ;

    private final String code;

    private final String name;

    /** 引擎bean 名称 */
    private final Class<? extends ScriptEngineService> engineClazz;

    /** 转化器名称 名称 */
    private final Class<? extends DataAnalysisService> convertClazz;
    ScriptType(String code, String name, Class<? extends ScriptEngineService> engineClazz, Class<? extends DataAnalysisService> convertClazz){
        this.code = code;
        this.name = name;
        this.engineClazz = engineClazz;
        this.convertClazz = convertClazz;
    }

    public static ScriptType ofCode (String code){
        for (ScriptType value : ScriptType.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return ScriptType.JS;
    }
}
