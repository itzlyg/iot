package cn.sinozg.applet.iot.script.service.impl;

import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.iot.common.constant.ProtocolContext;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.common.enums.ScriptType;
import cn.sinozg.applet.iot.protocol.model.DeviceMessageInfo;
import cn.sinozg.applet.iot.protocol.model.ProtoDeviceInfo;
import cn.sinozg.applet.iot.protocol.model.ThingServiceParams;
import cn.sinozg.applet.iot.script.factory.ScriptFactory;
import cn.sinozg.applet.iot.script.service.DataAnalysisService;
import cn.sinozg.applet.iot.script.service.ScriptEngineService;
import lombok.extern.slf4j.Slf4j;

/**
 * java 消息转化实现
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 17:07
 */
@Slf4j
public class JavaDataAnalysisServiceImpl implements DataAnalysisService {

    private final ScriptEngineService scriptEngine = ScriptFactory.scriptEngine(ScriptType.JAVA);
    @Override
    public void setAnalyScript(String script) {
        scriptEngine.setScript(script);
    }

    @Override
    public TmMessageInfo decode(DeviceMessageInfo params) {
        return scriptEngine.invokeMethod(TmMessageInfo.class, ProtocolContext.SCRIPT_METHOD_DECODE, params);
    }

    @Override
    public DeviceMessageInfo encode(ThingServiceParams<?> service, ProtoDeviceInfo device) {
        return scriptEngine.invokeMethod(DeviceMessageInfo.class, ProtocolContext.SCRIPT_METHOD_ENCODE, service, device);
    }

    @Override
    public void putScriptEnv(ScriptEnvType key, Object value) {
        scriptEngine.putScriptEnv(key, value);
    }
}
