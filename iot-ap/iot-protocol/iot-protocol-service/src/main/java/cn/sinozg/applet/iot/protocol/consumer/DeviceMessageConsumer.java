package cn.sinozg.applet.iot.protocol.consumer;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.com.enums.TmType;
import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.iot.common.enums.ProtocolTopicType;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.service.DeviceProtocolDataService;
import cn.sinozg.applet.iot.protocol.vo.response.DeviceInfoProtocolResponse;
import cn.sinozg.applet.mq.handle.ConsumerAutoHandler;
import cn.sinozg.applet.mq.joint.BaseTopicType;
import cn.sinozg.applet.mq.mq.MqConsumer;
import cn.sinozg.applet.mq.mq.MqProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 设备消息消费者
 *
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 21:33:57
 */
@Slf4j
@Service
public class DeviceMessageConsumer implements ConsumerAutoHandler<TmMessageInfo> {

    @Resource
    private DeviceProtocolDataService dataService;
    @Resource
    private MqProducer<TmMessageInfo> producer;
    @Resource
    private MqConsumer<TmMessageInfo> consumer;
    @Override
    public BaseTopicType type() {
        return ProtocolTopicType.THING_MODEL;
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
            log.info("DeviceMessageConsumer info, 消费者接收到的消息为： {}", msg);
            String type = msg.getType();
            ProtocolUtil.setTenantId(msg);
            // 重新发布属性入库消息
            if (TmType.eq(type, TmType.PROPERTY) && TmIdentifierType.eq(msg.getIdentifier(), TmIdentifierType.REPORT)) {
                producer.publish(ProtocolTopicType.DEVICE_PROPERTY, msg);
            }
            if (TmType.eq(type, TmType.CONFIG)) {
                // 重新发布设备配置消息，用于设备配置下发
                producer.publish(ProtocolTopicType.DEVICE_CONFIG, msg);
            }
            DeviceInfoProtocolResponse device = dataService.deviceInfoByCode(msg.getDeviceCode());
            if (device == null) {
                return;
            }
            msg.setUid(device.getUid());
            // 设备消息入库
            dataService.addModelMessage(msg);
        } catch (Throwable e) {
            log.error("device message consumer error", e);
        }
    }

}
