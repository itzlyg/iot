package cn.sinozg.applet.iot.script.service;

import cn.sinozg.applet.iot.common.enums.ScriptEnvType;

/**
 * 脚本引擎
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 17:09
 */
public interface ScriptEngineService {

    /**
     * 设置脚本
     * @param key 脚本
     */
    void setScript(String key);
    /**
     * 设置脚本
     * @param key 脚本类型
     * @param val 脚本 参数
     */
    void putScriptEnv(ScriptEnvType key, Object val);

    /**
     * 执行方法
     * @param methodName 方法
     * @param args 参数
     */
    void invokeMethod(String methodName, Object... args);

    /**
     * 执行方法
     * @param type 返回类型
     * @param methodName 方法
     * @param args 参数
     * @return 参数
     * @param <T> 返回类型
     */
    <T> T invokeMethod(Class<T> type, String methodName, Object... args);
}
