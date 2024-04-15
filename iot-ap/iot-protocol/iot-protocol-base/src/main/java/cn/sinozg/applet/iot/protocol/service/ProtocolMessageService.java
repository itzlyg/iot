package cn.sinozg.applet.iot.protocol.service;

import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.common.enums.ProtoMessageType;
import cn.sinozg.applet.iot.protocol.model.ReceiveMessageInfo;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 消息处理服务
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 16:43
 */
public interface ProtocolMessageService {


    /**
     * 添加脚本环境变量
     * @param key key
     * @param value value
     */
    void putScriptEnv(ScriptEnvType key, Object value);

    /**
     * 接收消息
     * @param headers 头
     * @param type 类型
     * @param msg 消息
     */
    void onReceive(Map<String, Object> headers, ProtoMessageType type, String msg);

    /**
     * 接收消息
     * @param headers 头
     * @param type 类型
     * @param msg 消息
     * @param consumer 函数
     */
    void onReceive(Map<String, Object> headers, ProtoMessageType type, String msg, Consumer<ReceiveMessageInfo> consumer);

}
