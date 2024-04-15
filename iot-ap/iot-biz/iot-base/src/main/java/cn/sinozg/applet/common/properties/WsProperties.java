package cn.sinozg.applet.common.properties;

import lombok.Data;

/**
 * websocket 配置
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-16 14:10
 */
@Data
public class WsProperties {

    /** 启动端口 */
    private int port = 8090;
    /** 是否ssl */
    private boolean ssl = false;
    /** ssl key */
    private String sslKey;
    /** ssl 密钥 */
    private String sslCert;
    /** 工作线程 */
    private int workerPoolSize = 20;
    /** 启动端口 */
    private int blockingPoolSize = 20;
    /** 服务地址 */
    private String apiUrl = "/api/ws/monitor";
}
