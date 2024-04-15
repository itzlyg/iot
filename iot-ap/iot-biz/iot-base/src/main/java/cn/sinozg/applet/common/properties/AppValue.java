package cn.sinozg.applet.common.properties;

import cn.sinozg.applet.common.constant.Constants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
* 自定义项目配置
* @Author: xyb
* @Description:
* @Date: 2022-11-14 下午 09:42
**/
@Data
@Component
@ConfigurationProperties(prefix = Constants.APP_PREFIX)
public class AppValue {

    /** 项目版本号 */
    private String version;
    /** 项目名称 */
    private String description;
    /** 前端显示图片的前缀 */
    private String fontUrl;
    /** 登录设置 */
    private LoginValue login;

    /** 微信设置 */
    private WechatValue wechat;

    /** 拦截器 **/
    private ApiUrlValue apiUrl;
    /** 租户 */
    private TenantValue tenant;

    /** 参数签名 */
    private SignValue sign;
    /** 文件存储 */
    private OssProperties oss;
    /** 协议相关配置 */
    private ProtocolProperties protocol;
    /** taos */
    private TdDsProperties taos;
    /** websocket */
    private WsProperties ws = new WsProperties();
}
