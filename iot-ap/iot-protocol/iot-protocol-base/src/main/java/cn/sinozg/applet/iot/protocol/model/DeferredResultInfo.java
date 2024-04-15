package cn.sinozg.applet.iot.protocol.model;

import lombok.Getter;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-09 15:15
 */
@Getter
public class DeferredResultInfo<T> {
    private DeferredResult<T> deferredResult;
    private boolean completed;
    private long completedTime;

    public DeferredResultInfo(){

    }

    public DeferredResultInfo(DeferredResult<T> deferredResult, boolean completed) {
        this.deferredResult = deferredResult;
        this.completed = completed;
        this.completedTime = System.currentTimeMillis();
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.completedTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
        // 完成超过3后视为过期，客户端可能已断开
        return completed && System.currentTimeMillis() - completedTime > 3 * 1000L;
    }
}
