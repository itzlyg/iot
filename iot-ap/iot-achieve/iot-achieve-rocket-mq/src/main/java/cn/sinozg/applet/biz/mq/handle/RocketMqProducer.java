package cn.sinozg.applet.biz.mq.handle;


import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.mq.joint.BaseTopicType;
import cn.sinozg.applet.mq.mq.MqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;

import java.nio.charset.StandardCharsets;

/**
 * 生产者
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 16:34:28
 */
@Slf4j
public class RocketMqProducer<T> implements MqProducer<T> {

    private final DefaultMQProducer producer;

    public RocketMqProducer(RocketMQProperties properties) {
        try {
            producer = new DefaultMQProducer(properties.getProducer().getGroup());
            producer.setNamesrvAddr(properties.getNameServer());
            producer.start();
        } catch (Throwable e) {
            log.error("初始化生产者错误", e);
            throw new CavException("初始化生产者错误！", e);
        }
    }

    @Override
    public void publish(BaseTopicType topic, T msg) {
        publish(topic.topic(), msg);
    }

    @Override
    public void publish(String topic, T msg) {
        try {
            producer.send(new Message(topic, JsonUtil.toJson(msg).getBytes(StandardCharsets.UTF_8)));
        } catch (Throwable e) {
            log.error("生产者发送消息失败", e);
            throw new CavException("生产者发送消息失败！", e);
        }
    }
}
