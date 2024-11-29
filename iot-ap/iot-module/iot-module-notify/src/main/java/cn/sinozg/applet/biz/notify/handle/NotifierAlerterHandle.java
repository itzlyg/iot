package cn.sinozg.applet.biz.notify.handle;

import cn.sinozg.applet.biz.notify.enums.NotifierType;
import cn.sinozg.applet.biz.notify.model.NotifierAlerterInfo;
import cn.sinozg.applet.biz.notify.model.NotifierTemplateInfo;
import cn.sinozg.applet.biz.notify.model.channel.NotifierConfigBaseInfo;

/**
 * @Author: xyb
 * @Description:
 * @Date: 2023-12-30 下午 01:03
 **/
public interface NotifierAlerterHandle<T extends NotifierConfigBaseInfo> {

    /**
     * 发送信息
     * @param params 配置参数
     * @param notifyTemplate 模板消息
     * @param alerter 告警信息
     */
    void send(T params, NotifierTemplateInfo notifyTemplate, NotifierAlerterInfo alerter);

    /**
     * 获取类型
     * @return 类型
     */
    NotifierType notifyType ();
}