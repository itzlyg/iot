package cn.sinozg.applet.biz.device.vo.request;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 设备分类信息表表 新增请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:48:09
*/
@Data
@Schema(name = "DeviceCategoryInfoCreateRequest", description = "设备分类信息表 新增请求参数")
public class DeviceCategoryInfoCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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

}
