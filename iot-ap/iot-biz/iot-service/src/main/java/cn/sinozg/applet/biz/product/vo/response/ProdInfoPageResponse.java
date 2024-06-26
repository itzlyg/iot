package cn.sinozg.applet.biz.product.vo.response;

import cn.sinozg.applet.common.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
* 产品消息表表 分页返回参数
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-11-27 21:44:29
*/
@Getter
@Setter
@Schema(name = "ProdInfoPageResponse", description = "产品消息表 分页返回参数")
public class ProdInfoPageResponse {

    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 名称：唯一标识物体的名称 */
    @Schema(description = "名称：唯一标识物体的名称")
    private String name;

    /** 图片ID：存储图片的ID字符串 */
    @Schema(description = "图片ID：存储图片的ID字符串")
    private String imgId;

    /** 分类ID：用于对物体进行分类 */
    @Schema(description = "分类ID：用于对物体进行分类")
    private String classifyId;

    @Schema(description = "分类name")
    private String classifyName;

    /** 协议ID：用于关联协议相关信息 */
    @Schema(description = "协议ID：用于关联协议相关信息")
    private String protocolId;

    @Schema(description = "协议Name")
    private String protocolName;

    /** 数据类型枚举：表示物体的类型 */
    @Schema(description = "数据类型枚举：表示物体的类型")
    private String typeId;

    /** 数据类型名称：与type_id一一对应，用于描述物体类型 */
    @Schema(description = "数据类型名称：与type_id一一对应，用于描述物体类型")
    private String typeName;

    /** 创建时间 */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createdTime;
}
