package cn.sinozg.applet.biz.notify.model.collect;

import io.netty.util.Timeout;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-22 19:02
 */
@Data
public class MetricsTime {
    private long startTime;
    private MetricsInfo metrics;
    private Timeout timeout;
}
