package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* 重置用户密码信息
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-03-24 14:52:38
*/
@Data
@Schema(name = "UserInfoResetPasswordRequest", description = "重置用户密码信息")
public class UserInfoResetPasswordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户id */
    @Schema(description = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    /** 新密码 */
    @Schema(description = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
