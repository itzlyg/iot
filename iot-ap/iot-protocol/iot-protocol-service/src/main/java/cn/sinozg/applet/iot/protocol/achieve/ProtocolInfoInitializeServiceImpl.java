package cn.sinozg.applet.iot.protocol.achieve;

import cn.sinozg.applet.common.service.FrameworkInitRunService;
import cn.sinozg.applet.common.utils.SpringUtil;
import cn.sinozg.applet.iot.protocol.service.ProtocolManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 初始化所有的消费者
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-17 14:10
 */
@Slf4j
@Service
public class ProtocolInfoInitializeServiceImpl implements FrameworkInitRunService {

    /**
     * 初始化所有的 消费者 和设备协议
     */
    @Override
    public void initInfo () {
        // 设备协议
        Map<String, ProtocolManagerService> protocolMap = SpringUtil.beansOfType(ProtocolManagerService.class);
        protocolMap.forEach((k, v) -> v.init());
    }
}
