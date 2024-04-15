package cn.sinozg.applet.biz.system.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
* 角色详细详细
* @Author: xyb
* @Description: 
* @Date: 2023-04-11 下午 08:58
**/
@Data
public class RoleInfoResponse {


    /** 主键 */
    @Schema(description = "主键")
    private String id;


    /** 用户id */
    @Schema(description = "用户id")
    private String userId;


    /** 角色编码 */
    @Schema(description = "角色编码")
    private String roleCode;


    /** 角色名称 */
    @Schema(description = "角色名称")
    private String roleName;


    /** 角色描述 */
    @Schema(description = "角色描述")
    private String roleDesc;


    /** 系统角色 */
    @Schema(description = "系统角色")
    private String sysRole;
}
