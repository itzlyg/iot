package cn.sinozg.applet.biz.system.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 用户菜单
* @Author: xyb
* @Description: 
* @Date: 2023-04-11 下午 09:05
**/
@Data
public class UserRoleResponse {
    /**
     * 角色编码
     */
    @Schema(description = "角色编码")
    private String roleCode;
    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

}
