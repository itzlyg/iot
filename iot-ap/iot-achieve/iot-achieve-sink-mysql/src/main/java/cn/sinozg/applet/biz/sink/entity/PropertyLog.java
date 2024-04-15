package cn.sinozg.applet.biz.sink.entity;

import cn.sinozg.applet.common.annotation.JsonField;
import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
* 产品属性日志记录表表
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-03-07 15:35:53
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sink_property_log")
@Schema(name = "PropertyLog", description = "产品属性日志记录表")
public class PropertyLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 产品key */
    @Schema(description = "产品key")
    @TableField("prod_key")
    private String prodKey;


    /** 设备编码 */
    @Schema(description = "设备编码")
    @TableField("device_code")
    private String deviceCode;


    /** 上报时间 */
    @Schema(description = "上报时间")
    @TableField("ts")
    private Long ts;


    /** 上报详情json */
    @Schema(description = "上报详情json")
    @TableField("record_json")
    @JsonField
    private Map<String, Object> recordJson;
}
