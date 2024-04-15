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
* 协议组件信息表
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-21 11:39:40
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_iot_protocol_module")
@Schema(name = "ProtocolModule", description = "协议组件信息")
public class ProtocolModule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 协议名称 */
    @Schema(description = "协议名称")
    @TableField("protocol_name")
    private String protocolName;


    /** 组件类型;01设备 02业务 */
    @Schema(description = "组件类型;01设备 02业务")
    @TableField("module_type")
    private String moduleType;


    /** 解析器类型;01自定义 02静态 */
    @Schema(description = "解析器类型;01自定义 02静态")
    @TableField("analysis_type")
    private String analysisType;


    /** 解析器id */
    @Schema(description = "解析器id")
    @TableField("analysis_id")
    private String analysisId;


    /** 协议类型 */
    @Schema(description = "协议类型")
    @TableField("protocol_type")
    private String protocolType;


    /** jar包id */
    @Schema(description = "jar包id")
    @TableField("jar_id")
    private String jarId;


    /** jar包name */
    @Schema(description = "jar包name")
    @TableField("jar_name")
    private String jarName;

    /** 组件脚本 类型 */
    @Schema(description = "组件脚本 类型")
    @TableField("protocol_script_type")
    private String protocolScriptType;


    /** 数据状态;00 未启用 01 启用 */
    @Schema(description = "数据状态;00 未启用 01 启用")
    @TableField("data_status")
    private String dataStatus;


    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;

}
