package cn.sinozg.applet.biz.mq.handle;


import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.mq.handle.ConsumerHandler;
import cn.sinozg.applet.mq.joint.BaseTopicType;
import cn.sinozg.applet.mq.mq.MqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;

/**
 * 消费者消费
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 16:33:26
 */
@Slf4j
public class RocketMqConsumer<T> implements MqConsumer<T> {

    private final String nameServer;

    private final Class<T> clazz;

    public  RocketMqConsumer(String nameServer, Class<T> clazz) {
        this.nameServer = nameServer;
        this.clazz = clazz;
    }

    @Override
    public void consume(BaseTopicType topic, ConsumerHandler<T> handler) {
        consume(topic.topic(), handler);
    }

    @Override
    public void consume(String topic, ConsumerHandler<T> handler) {
        String consumerGroup = StringUtils.uncapitalize(handler.getClass().getSimpleName());
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(nameServer);
        try {
            consumer.subscribe(topic, BaseConstants.ALL);
            consumer.registerMessageListener((MessageListenerConcurrently) (messages, context) -> {
                for (MessageExt message : messages) {
                    T msg = JsonUtil.toPojo(new String(message.getBody(), StandardCharsets.UTF_8), clazz);
                    handler.handler(msg);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (Throwable e) {
            log.error("消费者解析数据错误", e);
        }
    }
}
