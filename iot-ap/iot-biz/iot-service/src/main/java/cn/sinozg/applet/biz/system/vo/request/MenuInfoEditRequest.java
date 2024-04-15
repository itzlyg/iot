package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: xyb
 * @Description:
 * @Date: 2022-11-20 下午 08:37
 **/
@Data
public class MenuInfoEditRequest {

    /** 菜单ID */
    @Schema(description = "菜单ID")
    @NotBlank(message = "菜单ID不能为空")
    private String id;

    @NotBlank(message = "菜单名称不能为空")
    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单路由URL")
    private String menuUrl;

    @Schema(description = "菜单图标")
    private String menuIcon;

    @Schema(description = "菜单序号")
    private Integer seqNo;
}