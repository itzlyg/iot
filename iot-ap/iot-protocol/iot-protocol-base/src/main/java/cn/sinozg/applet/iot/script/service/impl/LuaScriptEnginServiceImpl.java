package cn.sinozg.applet.iot.script.service.impl;

import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.script.service.ScriptEngineService;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 17:28
 */
public class LuaScriptEnginServiceImpl implements ScriptEngineService {
    @Override
    public void setScript(String key) {

    }

    @Override
    public void putScriptEnv(ScriptEnvType key, Object val) {

    }

    @Override
    public void invokeMethod(String methodName, Object... args) {

    }

    @Override
    public <T> T invokeMethod(Class<T> type, String methodName, Object... args) {
        return null;
    }
}
