package cn.sinozg.applet.iot.script.service.impl;

import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.protocol.model.DeviceMessageInfo;
import cn.sinozg.applet.iot.protocol.model.ProtoDeviceInfo;
import cn.sinozg.applet.iot.protocol.model.ThingServiceParams;
import cn.sinozg.applet.iot.script.service.DataAnalysisService;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 15:32
 */
public class LuaDataAnalysisServiceImpl implements DataAnalysisService {
    @Override
    public void setAnalyScript(String script) {

    }

    @Override
    public TmMessageInfo decode(DeviceMessageInfo params) {
        return null;
    }

    @Override
    public DeviceMessageInfo encode(ThingServiceParams<?> service, ProtoDeviceInfo device) {
        return null;
    }

    @Override
    public void putScriptEnv(ScriptEnvType key, Object value) {

    }
}
