package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
* 角色新增修改实体
* @Author: xyb
* @Description: 
* @Date: 2023-04-11 下午 08:58
**/
@Data
public class RoleInfoRequest {

    @Schema(description = "主键ID")
    private String id;

    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "备注信息")
    private String roleDesc;
}
