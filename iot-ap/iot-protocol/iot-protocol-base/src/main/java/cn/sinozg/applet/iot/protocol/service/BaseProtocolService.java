package cn.sinozg.applet.iot.protocol.service;

import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.protocol.model.ProtocolConfig;

/**
 * 协议 基础接口
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 21:44:40
 */
public interface BaseProtocolService {

    String getId();

    void create(ProtocolConfig config);

    void start();

    void stop();

    void destroy();

    ProtocolConfig getConfig();
    /**
     * 添加脚本环境变量
     */
    void putScriptEnv(ScriptEnvType key, Object value);
}
