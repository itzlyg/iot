package cn.sinozg.applet.biz.sink.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 物模型数据记录表表
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 16:13:16
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sink_thing_model_message")
@Schema(name = "ThingModelMessage", description = "物模型数据记录表")
public class ThingModelMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 产品key */
    @Schema(description = "产品key")
    @TableField("prod_key")
    private String prodKey;


    /** 设备编号 */
    @Schema(description = "设备编号")
    @TableField("device_code")
    private String deviceCode;


    /** 设备名称 */
    @Schema(description = "设备名称")
    @TableField("device_name")
    private String deviceName;


    /** mid */
    @Schema(description = "mid")
    @TableField("mid")
    private String mid;


    /** mid名称 */
    @Schema(description = "mid名称")
    @TableField("mid_name")
    private String midName;


    /** 用户所属用户ID */
    @Schema(description = "用户所属用户ID")
    @TableField("uid")
    private String uid;


    /** 类型 */
    @Schema(description = "类型")
    @TableField("type")
    private String type;


    /** identifier */
    @Schema(description = "identifier")
    @TableField("identifier")
    private String identifier;

    /** 指令类型 */
    @Schema(description = "指令类型")
    @TableField("order_tp")
    private String orderTp;


    /** 状态码 */
    @Schema(description = "状态码")
    @TableField("code")
    private Integer code;


    /** 时间戳 */
    @Schema(description = "时间戳")
    @TableField("ts")
    private Long ts;


    /** 上报时间 */
    @Schema(description = "上报时间")
    @TableField("report_time")
    private Long reportTime;


    /** 数据 */
    @Schema(description = "数据")
    @TableField("data")
    private String data;
}
