package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 菜单分页
* @Author: xyb
* @Description:
* @Date: 2023-04-11 下午 10:27
**/
@Data
public class MenuLazyPageRequest {

    @Schema(description = "父级菜单编号")
    private String parCode;

    @Schema(description = "菜单编号")
    private String menuCode;

    @Schema(description = "菜单名称")
    private String menuName;
}
