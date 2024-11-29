package cn.sinozg.applet.biz.notify.model.proto;

import lombok.Getter;

/**
 * 收集器 枚举值
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-23 11:58
 */
@Getter
public enum CollectCode {

    /** SUCCESS */
    SUCCESS(0),
    /**
     * <pre>
     * collector not available
     * </pre>
     *
     * <code>UN_AVAILABLE = 1;</code>
     */
    UN_AVAILABLE(1),
    /**
     * <pre>
     * peer network un reachable(icmp)
     * </pre>
     *
     * <code>UN_REACHABLE = 2;</code>
     */
    UN_REACHABLE(2),
    /**
     * <pre>
     * peer network server un connectable(tcp,udp...)
     * </pre>
     *
     * <code>UN_CONNECTABLE = 3;</code>
     */
    UN_CONNECTABLE(3),
    /**
     * <pre>
     * collect metrics data failed(http,ssh,snmp...)
     * </pre>
     *
     * <code>FAIL = 4;</code>
     */
    FAIL(4),
    /**
     * <pre>
     * collect metrics data timeout
     * </pre>
     *
     * <code>TIMEOUT = 5;</code>
     */
    TIMEOUT(5),

    UNRECOGNIZED(-1),
    ;
    private final int code;

    CollectCode(int code){
        this.code = code;
    }
}
