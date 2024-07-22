package cn.sinozg.applet.iot.protocol.achieve;

import cn.sinozg.applet.common.utils.RedisUtil;
import cn.sinozg.applet.iot.common.constant.ProtocolRedisKey;
import cn.sinozg.applet.iot.protocol.service.BaseProtocolService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备路由 信息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 21:49:00
 */
@Component
public class DeviceRouter {

    private static final Map<String, BaseProtocolService> COMPONENTS = new HashMap<>();

    private String deviceRouterKey(String prodKey, String deviceCode) {
        return String.format(ProtocolRedisKey.DEVICE_ROUTER, prodKey, deviceCode);
    }

    public void putRouter(String prodKey, String deviceCode, BaseProtocolService component) {
        if (component == null) {
            return;
        }
        String comId = component.getId();
        if (!COMPONENTS.containsKey(component.getId())) {
            COMPONENTS.put(comId, component);
        }
        RedisUtil.setCacheObject(deviceRouterKey(prodKey, deviceCode), component.getId());
    }

    public void removeRouter(String prodKey, String deviceCode) {
        RedisUtil.deleteObject(deviceRouterKey(prodKey, deviceCode));
    }

    public BaseProtocolService getRouter(String prodKey, String deviceCode) {
        String comId = RedisUtil.getCacheObject(deviceRouterKey(prodKey, deviceCode));
        if (StringUtils.isBlank(comId)) {
            return null;
        }
        return COMPONENTS.get(comId);
    }
}
