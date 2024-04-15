package cn.sinozg.applet.iot.protocol.model;

import lombok.Getter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-09 15:13
 */
@Getter
public class DelayedPushInfo<T> implements Delayed {

    private final String topic;
    private final T msg;
    private final long addTime;

    public DelayedPushInfo(String topic, long addTime, T message) {
        this.topic = topic;
        this.addTime = addTime;
        this.msg = message;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(addTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long diff = o.getDelay(TimeUnit.NANOSECONDS) - getDelay(TimeUnit.NANOSECONDS);
        if (diff == 0) {
            return 0;
        }
        return diff > 0 ? 1 : -1;
    }
}