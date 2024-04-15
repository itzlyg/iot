package cn.sinozg.applet.biz.system.vo.response;

import cn.sinozg.applet.common.annotation.PicUrl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
* 用户详细详细
* @Author: xyb
* @Description: 
* @Date: 2023-04-11 下午 08:58
**/
@Data
public class UserInfoDetailResponse {

    /** 用户ID */
    @Schema(description = "用户ID")
    private String id;
    /** 用户账号 */
    @Schema(description = "用户账号")
    private String userName;

    /** 用户昵称 */
    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "头像id")
    private String avatar;

    @PicUrl
    @Schema(description = "头像 地址")
    private String avatarUrl;

    /** 用户邮箱 */
    @Schema(description = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Schema(description = "手机号码")
    private String phoneNum;

    /** 部门id */
    @Schema(description = "部门id")
    private String deptId;

    /** 用户性别;00男 01女 02未知 */
    @Schema(description = "用户性别;00男 01女 02未知")
    private String sex;

    /** 帐号状态;00正常 01停用 */
    @Schema(description = "帐号状态;00正常 01停用")
    private String dataStatus;

    @Schema(description = "角色集合")
    private List<String> roleIds;

    @Schema(description = "备注")
    private String remark;
}
