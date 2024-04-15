package cn.sinozg.applet.iot.common.constant;

import cn.sinozg.applet.common.constant.BaseRedisKeys;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 14:21
 */
public class ProtocolRedisKey extends BaseRedisKeys {

    /** 设备路由 **/
    public static final String DEVICE_ROUTER = REDIS_PREFIX + "router:%s:%s";

    public static final String KEY_CMD_MID = REDIS_PREFIX + "cmd:mid:%s:%s";
    /** 设备命令id **/
    public static final String UNIQUE_ID = REDIS_PREFIX + "unique:id:%s";
}
