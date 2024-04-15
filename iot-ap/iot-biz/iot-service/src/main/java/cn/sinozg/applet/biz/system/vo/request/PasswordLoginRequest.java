package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: xyb
 * @Description:
 * @Date: 2023-03-24 下午 01:01
 **/
@Data
public class PasswordLoginRequest {

    @NotBlank(message = "账号不能为空！")
    @Schema(description = "用户")
    private String userName;
    @NotBlank(message = "密码不能为空！")
    @Schema(description = "密码")
    private String password;

    private String uid;

    @NotBlank(message = "租户id为空！")
    @Schema(description = "租户id")
    private String tenantId;
}