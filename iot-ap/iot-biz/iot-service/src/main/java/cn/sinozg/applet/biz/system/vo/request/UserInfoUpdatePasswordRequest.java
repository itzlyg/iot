package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* 修改本人的用户密码信息
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-03-24 14:52:38
*/
@Data
@Schema(name = "UserInfoUpdatePasswordRequest", description = "修改本人的用户密码信息")
public class UserInfoUpdatePasswordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 旧密码 */
    @Schema(description = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /** 新密码 */
    @Schema(description = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    /** 新密码确认 */
    @Schema(description = "新密码确认")
    @NotBlank(message = "新密码确认不能为空")
    private String confirmPassword;
}
