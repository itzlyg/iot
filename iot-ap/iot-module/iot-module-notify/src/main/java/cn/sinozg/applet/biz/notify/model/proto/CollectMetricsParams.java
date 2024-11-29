package cn.sinozg.applet.biz.notify.model.proto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 收集器 指标参数
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-23 11:58
 */
@Getter
@Setter
@Builder
public class CollectMetricsParams {

    private String id;

    private String app;

    private String tenantId;

    private int priority;

    private String metrics;

    private long time;

    private String msg;

    private CollectCode code;

    private List<CollectRowsParams> value;

    private List<CollectFieldParams> fields;

    public int getFieldsCount(){
        if (fields == null) {
            return 0;
        }
        return fields.size();
    }

    public int getValueCount(){
        if (value == null) {
            return 0;
        }
        return value.size();
    }

}
