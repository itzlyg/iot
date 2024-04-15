package cn.sinozg.applet.biz.system.vo.response;

import cn.sinozg.applet.common.annotation.DictTrans;
import cn.sinozg.applet.common.annotation.PicUrl;
import cn.sinozg.applet.common.enums.DictType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 用户信息表表 分页返回参数
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-03-24 14:52:38
*/
@Data
@Schema(name = "UserInfoPageResponse", description = "用户信息表 分页返回参数")
public class UserInfoPageResponse implements Serializable {

    /** 用户ID */
    @Schema(description = "用户ID")
    private String id;

    /** 头像 */
    @PicUrl
    @Schema(description = "头像")
    private String avatar;

    /** 用户账号 */
    @Schema(description = "用户账号")
    private String userName;

    /** 用户昵称 */
    @Schema(description = "用户昵称")
    private String nickName;

    /** 部门Name */
    @Schema(description = "部门Name")
    private String deptName;

    /** 用户邮箱 */
    @Schema(description = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Schema(description = "手机号码")
    private String phoneNum;

    @Schema(description = "角色名称")
    private String roleNameStr;

    /** 帐号状态;00正常 01停用 */
    @Schema(description = "帐号状态;00 未启用 01 启用")
    @DictTrans(type = DictType.DATA_STATUS)
    private String dataStatus;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
