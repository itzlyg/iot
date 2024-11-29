package cn.sinozg.applet.biz.notify.properties;

import cn.sinozg.applet.common.constant.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 告警配置
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-19 16:11:58
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = Constants.APP_PREFIX + ".alerter")
public class AlerterProperties {

    /** 系统看板地址 */
    private String consoleUrl = "https://www.sinozg.cn/";

    /** 企业微信 机器人 url */
    private String wwRobotUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=";
    /** 企业微信 app url */
    private String wwAppUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    /** 钉钉 webhook url */
    private String dingTalkUrl = "https://oapi.dingtalk.com/robot/send?access_token=";

    /** 飞书 webhook url */
    private String feiShuUrl = "https://open.feishu.cn/open-apis/bot/v2/hook/";

    /** 企业微信获取到 token */
    private String wwSecretUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

}
