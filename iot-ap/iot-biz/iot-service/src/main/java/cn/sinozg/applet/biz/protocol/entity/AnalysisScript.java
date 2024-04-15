package cn.sinozg.applet.biz.protocol.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 数据解析脚本表
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_iot_analysis_script")
@Schema(name = "AnalysisScript", description = "数据解析脚本")
public class AnalysisScript extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 脚本名称 */
    @Schema(description = "脚本名称")
    @TableField("script_name")
    private String scriptName;


    /** 脚本类型 */
    @Schema(description = "脚本类型")
    @TableField("script_type")
    private String scriptType;


    /** 脚本说明 */
    @Schema(description = "脚本说明")
    @TableField("script_desc")
    private String scriptDesc;


    /** 数据状态;00 未启用 01 启用 */
    @Schema(description = "数据状态;00 未启用 01 启用")
    @TableField("data_status")
    private String dataStatus;


    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;

}
