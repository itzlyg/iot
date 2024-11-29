package cn.sinozg.applet.biz.notify.model.proto;

import lombok.Builder;
import lombok.Data;

/**
 * 收集器 字段参数
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-23 11:58
 */
@Data
@Builder
public class CollectFieldParams {

    private String name;

    private int type;

    private boolean label;

    private String unit;
}
