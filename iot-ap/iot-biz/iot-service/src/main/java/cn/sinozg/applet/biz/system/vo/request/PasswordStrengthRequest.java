package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 密码强度校验
 * @Author: xyb
 * @Description:
 * @Date: 2023-05-11 下午 11:04
 **/
@Data
public class PasswordStrengthRequest {
    /** 密码 */
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}