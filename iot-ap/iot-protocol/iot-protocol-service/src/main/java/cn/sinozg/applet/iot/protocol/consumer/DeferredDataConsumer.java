package cn.sinozg.applet.iot.protocol.consumer;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.com.enums.TmType;
import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.ThreadPool;
import cn.sinozg.applet.iot.common.enums.ProtocolTopicType;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.model.DeferredResultInfo;
import cn.sinozg.applet.iot.protocol.model.DelayedPushInfo;
import cn.sinozg.applet.iot.protocol.model.DeviceBaseInfo;
import cn.sinozg.applet.mq.handle.ConsumerAutoHandler;
import cn.sinozg.applet.mq.joint.BaseTopicType;
import cn.sinozg.applet.mq.mq.MqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * 长连接推送消息消费
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-09 15:10:55
 */
@Slf4j
@Component
public class DeferredDataConsumer implements ConsumerAutoHandler<TmMessageInfo> {

    private final Map<String, Set<String>> topicConsumers = new ConcurrentHashMap<>();
    private final Map<String, DeferredResultInfo<TmMessageInfo>> consumerDeferred = new ConcurrentHashMap<>();
    private final DelayQueue<DelayedPushInfo<TmMessageInfo>> delayedPushes = new DelayQueue<>();

    @Resource
    private MqConsumer<TmMessageInfo> consumer;
    @Override
    public void initialization() {
        ThreadPool.execute(() -> {
            while (true) {
                try {
                    DelayedPushInfo<TmMessageInfo> delayedPush = delayedPushes.take();
                    TmMessageInfo modelMessage = delayedPush.getMsg();
                    publish(delayedPush.getTopic(), modelMessage, true);
                } catch (Throwable e) {
                    log.error("delayed push error", e);
                }
            }
        });
    }

    @Override
    public MqConsumer<TmMessageInfo> consumer() {
        return consumer;
    }

    @Override
    public void handler(TmMessageInfo msg) {
        log.info("长连接推送消息消费...接收到的消息为：{}", msg);
        String type = msg.getType();
        String identifier = msg.getIdentifier();
        // 属性上报和上下线消息
        boolean report = TmType.eq(type, TmType.PROPERTY) && TmIdentifierType.eq(identifier, TmIdentifierType.REPORT);
        if (report || TmType.eq(type, TmType.STATE)) {
            ProtocolUtil.setTenantId(msg);
            publish(msg.getDeviceCode(), msg);
        }
    }

    @Override
    public BaseTopicType type() {
        return ProtocolTopicType.THING_MODEL;
    }


    /**
     * 新增一个消费者
     * @param consumerId 消费者id
     * @param topic topic
     * @return DeferredResult
     */
    public DeferredResult<TmMessageInfo> addConsumer(String consumerId, String topic) {
        topic = getTopic(topic);
        topicConsumers.putIfAbsent(topic, new HashSet<>());
        Set<String> consumers = topicConsumers.get(topic);
        consumers.add(consumerId);
        String consumerKey = getConsumerKey(consumerId, topic);
        DeferredResult<TmMessageInfo> result = new DeferredResult<>(10000L, new DeviceBaseInfo());
        DeferredResultInfo<TmMessageInfo> resultInfo = new DeferredResultInfo<>(result, false);
        result.onCompletion(() -> resultInfo.setCompleted(true));
        result.onTimeout(() -> resultInfo.setCompleted(true));

        consumerDeferred.put(consumerKey, resultInfo);
        return result;
    }

    public void publish(String topic, TmMessageInfo msg) {
        publish(topic, msg, false);
    }

    public void publish(String topic, TmMessageInfo msg, boolean republish) {
        topic = getTopic(topic);
        Set<String> consumers = topicConsumers.get(topic);
        if (consumers == null) {
            return;
        }
        for (String consumer : consumers) {
            String consumerKey = getConsumerKey(consumer, topic);
            DeferredResultInfo<TmMessageInfo> result = consumerDeferred.get(consumerKey);
            if (result == null) {
                continue;
            }
            // 如果已经推送完成了，等待1秒再尝试发送，让客户端有时间重连
            if (!republish && result.isCompleted() && !result.isExpired()) {
                delayedPushes.offer(new DelayedPushInfo<>(topic, System.currentTimeMillis(), msg), 3, TimeUnit.SECONDS);
            } else {
                log.info("push {} to {},msg:{}", topic, consumer, JsonUtil.toJson(msg));
                result.getDeferredResult().setResult(msg);
            }
        }
    }

    /**
     * 处理 topic
     * @param topic topic
     * @return topic
     */
    private String getTopic(String topic){
        if (!StringUtils.startsWith(topic, ProtocolTopicType.DEVICE_INFO.getCode())) {
            return ProtocolTopicType.DEVICE_INFO.getCode() + topic;
        }
        return topic;
    }

    private String getConsumerKey(String consumerId, String topic) {
        return consumerId + topic;
    }
}
