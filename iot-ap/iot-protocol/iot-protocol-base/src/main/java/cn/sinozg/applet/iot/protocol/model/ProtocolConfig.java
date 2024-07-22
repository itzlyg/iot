package cn.sinozg.applet.iot.protocol.model;

import lombok.Data;

/**
 * 其他配置信息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 21:45:38
 */
@Data
public class ProtocolConfig {

    private long cmdTimeout;

    private String other;
}
