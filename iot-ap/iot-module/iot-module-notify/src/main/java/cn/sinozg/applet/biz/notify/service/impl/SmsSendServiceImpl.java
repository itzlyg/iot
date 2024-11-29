package cn.sinozg.applet.biz.notify.service.impl;

import cn.sinozg.applet.biz.notify.service.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 15:04
 */
@Slf4j
@Service
public class SmsSendServiceImpl implements SmsSendService {

    @Override
    public void sendMessage(List<String> phones, Map<String, String> params) {
        log.info("sendMessage ..");
    }
}
