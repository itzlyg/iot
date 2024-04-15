package cn.sinozg.applet.biz.system.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 菜单分页懒加载返回
* @Author: xyb
* @Description:
* @Date: 2023-04-11 下午 10:28
**/
@Data
public class MenuLazyPageResponse {

    @Schema(description = "菜单id")
    private String id;

    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单地址")
    private String menuUrl;

    @Schema(description = "菜单icon")
    private String menuIcon;

    @Schema(description = "父级菜单编码")
    private String parentMenuCode;

    @Schema(description = "顺序号")
    private Integer seqNo;

    @Schema(description = "菜单类型;01 菜单 02按钮")
    private String menuType;

    @Schema(description = "渠道 01:web 02:手机")
    private String channel;

    @Schema(description = "是否有子菜单")
    private boolean hasChildren;
}
