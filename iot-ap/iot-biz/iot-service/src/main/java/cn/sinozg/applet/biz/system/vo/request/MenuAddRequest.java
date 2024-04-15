package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
* 新增菜单信息
* @Author: xyb
* @Description:
* @Date: 2023-04-11 下午 09:43
**/
@Data
public class MenuAddRequest {

    @NotBlank(message = "菜单名称不能为空")
    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单路由URL")
    private String menuUrl;

    @Schema(description = "菜单父级编码")
    private String parentCode;

    @Schema(description = "菜单图标")
    private String menuIcon;

    @NotBlank(message = "渠道不能为空")
    @Schema(description = "渠道")
    private String channel;

    @Schema(description = "菜单序号")
    private Integer seqNo;

    @NotBlank(message = "菜单类型不能为空")
    @Schema(description = "菜单类型")
    private String menuType;
}
