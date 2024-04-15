package cn.sinozg.applet.iot.script.service;

import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.protocol.model.DeviceMessageInfo;
import cn.sinozg.applet.iot.protocol.model.ProtoDeviceInfo;
import cn.sinozg.applet.iot.protocol.model.ThingServiceParams;

/**
 * 消息数据 转化器 用来解析 、编码数据
 * convert.js 脚本或者 convert.spi
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 16:58
 */
public interface DataAnalysisService {

    void setAnalyScript(String script);

    void putScriptEnv(ScriptEnvType key, Object value);

    TmMessageInfo decode(DeviceMessageInfo params);

    DeviceMessageInfo encode(ThingServiceParams<?> service, ProtoDeviceInfo device);
}
