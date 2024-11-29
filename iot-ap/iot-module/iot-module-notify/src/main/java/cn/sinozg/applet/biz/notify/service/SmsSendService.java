package cn.sinozg.applet.biz.notify.service;

import java.util.List;
import java.util.Map;

/**
 * 短信发送
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 17:35
 */
public interface SmsSendService {

    /**
     * 发送短信
     * @param phones 电话号码
     * @param params 参数
     */
    default void sendMessage(List<String> phones, Map<String, String> params) {}

}
