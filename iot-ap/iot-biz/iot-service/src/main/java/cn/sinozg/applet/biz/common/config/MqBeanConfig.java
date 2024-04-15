package cn.sinozg.applet.biz.common.config;


import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.mq.mq.MqConsumer;
import cn.sinozg.applet.mq.mq.MqProducer;
import cn.sinozg.applet.mq.vertx.handle.VertxMqConsumer;
import cn.sinozg.applet.mq.vertx.handle.VertxMqProducer;
import cn.sinozg.applet.ws.model.WebSocketData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  mq bean 配置
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-20 12:37:10
 */
@Configuration
public class MqBeanConfig {

    @Bean(name = "producer")
    public MqProducer<TmMessageInfo> producer() {
        return new VertxMqProducer<>(TmMessageInfo.class);
    }

    @Bean(name = "consumer")
    public MqConsumer<TmMessageInfo> consumer() {
        return new VertxMqConsumer<>(TmMessageInfo.class);
    }

    @Bean(name = "socketProducer")
    public MqProducer<WebSocketData> socketProducer() {
        return new VertxMqProducer<>(WebSocketData.class);
    }

    @Bean(name = "socketConsumer")
    public MqConsumer<WebSocketData> socketConsumer() {
        return new VertxMqConsumer<>(WebSocketData.class);
    }
}
