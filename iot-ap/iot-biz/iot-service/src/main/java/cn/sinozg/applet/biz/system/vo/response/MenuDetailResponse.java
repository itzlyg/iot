package cn.sinozg.applet.biz.system.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 菜单详情
* @Author: xyb
* @Description:
* @Date: 2023-04-12 下午 09:54
**/
@Data
public class MenuDetailResponse {


    /** 主键 */
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 菜单编码 */
    @Schema(description = "菜单编码")
    private String menuCode;

    /** 顺序号 */
    @Schema(description = "顺序号")
    private Integer seqNo;


    /** 菜单名称 */
    @Schema(description = "菜单名称")
    private String menuName;


    /** 菜单地址 */
    @Schema(description = "菜单地址")
    private String menuUrl;


    /** 菜单图标 */
    @Schema(description = "菜单图标")
    private String menuIcon;


    /** 菜单类型;01 菜单 02按钮 */
    @Schema(description = "菜单类型;01 菜单 02按钮")
    private String menuType;


    /** 渠道 01:web 02:手机 */
    @Schema(description = "渠道 01:web 02:手机")
    private String channel;
}