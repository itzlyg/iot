package cn.sinozg.applet.biz.notify.model.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 邮件配置 需要接收人
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-19 14:42
 */
@Getter
@Setter
public class NotifierEmailConfig extends NotifierConfigBaseInfo {

    @Schema(description = "邮箱ip")
    @NotBlank(message = "邮箱ip不能为空！")
    private String host;
    @Schema(description = "端口")
    private int port;
    @Schema(description = "姓名")
    @NotBlank(message = "邮箱地址不能为空！")
    private String userName;
    @Schema(description = "密码")
    @NotBlank(message = "邮箱密码不能为空！")
    private String password;
    @Schema(description = "ssl")
    private boolean sslEnable;
    @Schema(description = "发送人")
    private String fromUser;
}
