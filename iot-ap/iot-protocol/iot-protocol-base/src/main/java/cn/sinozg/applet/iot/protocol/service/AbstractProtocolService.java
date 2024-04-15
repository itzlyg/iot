package cn.sinozg.applet.iot.protocol.service;

import cn.sinozg.applet.common.utils.SnowFlake;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.script.service.DataAnalysisService;
import cn.sinozg.applet.iot.script.service.ScriptEngineService;
import cn.sinozg.applet.iot.protocol.model.DeviceAuthInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceRegisterInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceStateInfo;
import cn.sinozg.applet.iot.protocol.model.ProtocolConfig;

/**
 * 设备协议基础类 实现了协议注册的基础方法
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 16:56:35
 */
public abstract class AbstractProtocolService implements ProtocolMethodService {

    /** 消息发送器 */
    private ProtocolMessageService messageService;
    /** 数据解析器 */
    private DataAnalysisService analysisService;

    /** 协议的配置信息 */
    private ProtocolConfig config;
    /** 协议脚本 */
    private ScriptEngineService protocolScript;

    protected String id;

    @Override
    public void create(ProtocolConfig config) {
        this.config = config;
        this.id = SnowFlake.genId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void onDeviceRegister(DeviceRegisterInfo info) {
    }

    @Override
    public void onDeviceAuth(DeviceAuthInfo info) {
    }

    @Override
    public void onDeviceStateChange(DeviceStateInfo state) {
    }

    @Override
    public ProtocolConfig getConfig() {
        return config;
    }

    @Override
    public void putScriptEnv(ScriptEnvType key, Object value) {
    }

    @Override
    public DataAnalysisService getAnalysisService() {
        return analysisService;
    }

    @Override
    public void setAnalysisService(DataAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @Override
    public void setMessageService(ProtocolMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public ProtocolMessageService getMessageService() {
        return messageService;
    }

    @Override
    public ScriptEngineService getProtocolScript() {
        return protocolScript;
    }

    @Override
    public void setProtocolScript(ScriptEngineService protocolScript) {
        this.protocolScript = protocolScript;
    }
}
