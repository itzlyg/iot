package cn.sinozg.applet.biz.device.vo.response;

import cn.sinozg.applet.common.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 设备分类信息表表 分页返回参数
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:48:09
*/
@Data
@Schema(name = "DeviceCategoryInfoPageResponse", description = "设备分类信息表 分页返回参数")
public class DeviceCategoryInfoPageResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 产品分类代码 */
    @Schema(description = "产品分类代码")
    private String categoryCode;

    /** 父级产品分类代码 */
    @Schema(description = "父级产品分类代码")
    private String parentCode;

    /** 产品分类名称 */
    @Schema(description = "产品分类名称")
    private String categoryName;

    /** 产品分类说明 */
    @Schema(description = "产品分类说明")
    private String categoryDesc;

    /** 顺序号 */
    @Schema(description = "顺序号")
    private Integer seqNo;

    /** 是否有效0无效1有效 */
    @Schema(description = "是否有效0无效1有效")
    private Integer isValid;

    @Schema(description = "是否有子级")
    private boolean hasChildren;

    /** 创建时间 */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createdTime;
}
