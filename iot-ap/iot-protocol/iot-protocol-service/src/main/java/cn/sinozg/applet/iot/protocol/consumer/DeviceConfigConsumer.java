package cn.sinozg.applet.iot.protocol.consumer;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.com.enums.TmType;
import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.iot.common.enums.ProtocolTopicType;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.model.DeviceConfigInfo;
import cn.sinozg.applet.iot.protocol.model.ThingServiceParams;
import cn.sinozg.applet.iot.protocol.service.DeviceProtocolDataService;
import cn.sinozg.applet.iot.protocol.service.ProtocolManagerService;
import cn.sinozg.applet.mq.handle.ConsumerAutoHandler;
import cn.sinozg.applet.mq.joint.BaseTopicType;
import cn.sinozg.applet.mq.mq.MqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 设备配置服务
 *
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 20:55:42
 */
@Slf4j
@Service
public class DeviceConfigConsumer implements ConsumerAutoHandler<TmMessageInfo> {

    @Resource
    private ProtocolManagerService protocolManager;

    @Resource
    private DeviceProtocolDataService dataService;

    @Resource
    private MqConsumer<TmMessageInfo> consumer;

    @Override
    public BaseTopicType type() {
        return ProtocolTopicType.DEVICE_CONFIG;
    }

    @Override
    public void initialization() {

    }

    @Override
    public MqConsumer<TmMessageInfo> consumer() {
        return consumer;
    }

    @Override
    public void handler(TmMessageInfo msg) {
        try {
            String identifier = msg.getIdentifier();
            if (TmIdentifierType.eq(identifier, TmIdentifierType.GET)) {
                ProtocolUtil.setTenantId(msg);
                //收到设备获取配置消息，回复配置信息给设备
                DeviceConfigInfo deviceConfig = dataService.deviceConfigById(msg.getDeviceCode());
                if (deviceConfig == null) {
                    return;
                }
                Map<String, ?> config = JsonUtil.toMap(deviceConfig.getConfig());
                ThingServiceParams<Object> service = new ThingServiceParams<>();
                service.setDeviceCode(msg.getDeviceCode());
                service.setProdKey(msg.getProdKey());
                // 回复
                service.setIdentifier(TmIdentifierType.reply(TmIdentifierType.GET));
                service.setType(TmType.CONFIG);
                service.setMid(msg.getMid());
                service.setParams(config);

                protocolManager.send(service);
            }
        } catch (Throwable e) {
            log.error("consumer device config msg error", e);
        }
    }


}
