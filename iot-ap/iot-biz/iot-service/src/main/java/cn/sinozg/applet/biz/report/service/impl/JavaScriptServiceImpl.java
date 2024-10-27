package cn.sinozg.applet.biz.report.service.impl;

import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.iot.common.constant.ProtocolContext;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.protocol.model.DeviceMessageInfo;
import cn.sinozg.applet.iot.protocol.model.ProtoDeviceInfo;
import cn.sinozg.applet.iot.protocol.model.ThingServiceParams;
import cn.sinozg.applet.iot.script.service.DataAnalysisService;
import org.springframework.stereotype.Service;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-10-27 15:05
 */
@Service(value = ProtocolContext.JAVA_ANALYSIS_BEAN)
public class JavaScriptServiceImpl implements DataAnalysisService {
    @Override
    public void setAnalyScript(String script) {

    }

    @Override
    public void putScriptEnv(ScriptEnvType key, Object value) {

    }

    @Override
    public TmMessageInfo decode(DeviceMessageInfo params) {
        return null;
    }

    @Override
    public DeviceMessageInfo encode(ThingServiceParams<?> service, ProtoDeviceInfo device) {
        return null;
    }
}
