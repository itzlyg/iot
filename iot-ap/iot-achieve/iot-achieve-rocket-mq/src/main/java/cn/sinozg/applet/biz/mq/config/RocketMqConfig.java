package cn.sinozg.applet.biz.mq.config;

import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.biz.mq.handle.RocketMqConsumer;
import cn.sinozg.applet.biz.mq.handle.RocketMqProducer;
import cn.sinozg.applet.mq.mq.MqConsumer;
import cn.sinozg.applet.mq.mq.MqProducer;
import cn.sinozg.applet.ws.model.WebSocketData;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 消息中间件配置
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 16:36:07
 */
@Configuration
public class RocketMqConfig {

    @Resource
    private RocketMQProperties properties;

    @Bean(name = "producer")
    public MqProducer<TmMessageInfo> producer() {
        return new RocketMqProducer<>(properties);
    }

    @Bean(name = "consumer")
    public MqConsumer<TmMessageInfo> consumer() {
        return new RocketMqConsumer<>(properties.getNameServer(), TmMessageInfo.class);
    }

    @Bean(name = "socketProducer")
    public MqProducer<WebSocketData> socketProducer() {
        return new RocketMqProducer<>(properties);
    }

    @Bean(name = "socketConsumer")
    public MqConsumer<WebSocketData> socketConsumer() {
        return new RocketMqConsumer<>(properties.getNameServer(), WebSocketData.class);
    }
}
