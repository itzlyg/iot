package cn.sinozg.applet.iot.mqtt.service.impl;

import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.iot.common.constant.ProtocolContext;
import cn.sinozg.applet.iot.common.enums.ScriptEnvType;
import cn.sinozg.applet.iot.protocol.model.DeviceMessageInfo;
import cn.sinozg.applet.iot.protocol.model.MessageOnReceiveResult;
import cn.sinozg.applet.iot.protocol.model.ProtoDeviceInfo;
import cn.sinozg.applet.iot.protocol.model.ThingServiceParams;
import cn.sinozg.applet.iot.script.service.DataAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-04-15 20:41
 */
@Slf4j
@Service(value = ProtocolContext.JAVA_ANALYSIS_BEAN)
public class JavaScriptServiceImpl implements DataAnalysisService {


    @Override
    public void setAnalyScript(String script) {

    }

    @Override
    public void putScriptEnv(ScriptEnvType key, Object value) {

    }

    /**
     * <p>接收mq 上报上来的数据 处理</p>
     * 逻辑：
     * <p>mqtt 订阅对应的topic 后 在此方法处理 上报上来的指令</p>
     * <p>根据不同的类型 封装数据 返回对应的结果  对应的bean的 的map</p>
     * <p>如果是上报，ota等 回回调解码decode数据</p>
     * <p>再根据 实际类型回调，behaviourService 业务实现回调逻辑</p>
     * <p>先处理返回的数据，如果实体为空，回调订阅函数直接返回，否则 回调业务函数 如果有ack 再发送ack消息， 再回调订阅的函数</p>
     *
     * @param headers 请求头
     * @param type 类型
     * @param msg 消息
     * @return 处理结果 根据不同的类型 返回对应的结果 对应的bean 的map
     */
    public MessageOnReceiveResult onReceive (Map<String, Object> headers, String type, String msg){
        MessageOnReceiveResult result = new MessageOnReceiveResult();
        // TODO
        return result;
    }

    @Override
    public TmMessageInfo decode(DeviceMessageInfo params) {

        // TODO
        return null;
    }

    @Override
    public DeviceMessageInfo encode(ThingServiceParams<?> service, ProtoDeviceInfo device) {
        log.info("encode data {}", service);
        DeviceMessageInfo info = new DeviceMessageInfo();
        // TODO
        return info;
    }



}
