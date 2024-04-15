package cn.sinozg.applet.biz.system.vo.request;

import cn.sinozg.applet.common.annotation.Regex;
import cn.sinozg.applet.common.enums.RegexType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
* 用户信息表表 新增请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-03-24 14:52:38
*/
@Data
@Schema(name = "UserInfoCreateRequest", description = "用户信息表 新增请求参数")
public class UserInfoCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(description = "头像id")
    private String avatar;

    /** 用户账号 */
    @Schema(description = "用户账号")
    @NotBlank(message = "用户账号不能为空")
    private String userName;

    /** 用户昵称 */
    @Schema(description = "用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    private String nickName;

    /** 用户邮箱 */
    @Schema(description = "用户邮箱")
    @Regex(type = RegexType.EMAIL)
    private String email;

    /** 手机号码 */
    @Schema(description = "手机号码")
    private String phoneNumber;

    /** 校区id */
    @Schema(description = "校区id")
    private String campusId;
    /** 部门id */
    @Schema(description = "部门id")
    private String deptId;

    @Schema(description = "角色集合")
    private List<String> roleIds;
}
