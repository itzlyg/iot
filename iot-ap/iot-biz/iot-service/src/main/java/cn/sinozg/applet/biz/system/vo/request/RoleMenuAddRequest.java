package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
* 角色新增
* @Author: xyb
* @Description: 
* @Date: 2023-04-11 下午 08:52
**/
@Data
public class RoleMenuAddRequest {

    @NotBlank(message = "角色编码不能为空")
    @Schema(description = "角色编码")
    private String roleCode;

    @NotBlank(message = "渠道不能为空")
    @Schema(description = "渠道")
    private String channel;

    @Schema(description = "菜单编码list")
    private List<String> menuCodeList;
}
