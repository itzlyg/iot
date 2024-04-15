package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* 重置用户密码信息 通过验证码的方式
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-03-24 14:52:38
*/
@Data
@Schema(name = "UserInfoCodePasswordRequest", description = "通过验证码的方式")
public class UserInfoCodePasswordRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 邮箱或者手机号 */
    @Schema(description = "邮箱或者手机号")
    @NotBlank(message = "邮箱或者手机号不能为空")
    private String emailPhone;
    /** 验证码 */
    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;

    /** 新密码 */
    @Schema(description = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    /** 新密码确认 */
    @Schema(description = "新密码确认")
    @NotBlank(message = "新密码确认不能为空")
    private String confirmPassword;
}
