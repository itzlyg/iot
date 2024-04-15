package cn.sinozg.applet.biz.device.entity;

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
* 设备信息表表
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_set_device_info")
@Schema(name = "DeviceInfo", description = "设备信息表")
public class DeviceInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 设备编码 */
    @Schema(description = "设备编码")
    @TableField("device_code")
    private String deviceCode;


    /** 设备名称 */
    @Schema(description = "设备名称")
    @TableField("device_name")
    private String deviceName;


    /** 产品 key */
    @Schema(description = "产品 key")
    @TableField("prod_key")
    private String prodKey;


    /** 设备类型 */
    @Schema(description = "设备类型")
    @TableField("device_type")
    private String deviceType;


    /** 顺序号 */
    @Schema(description = "顺序号")
    @TableField("seq_no")
    private Integer seqNo;


    /** 是否有效0无效1有效 */
    @Schema(description = "是否有效0无效1有效")
    @TableField("is_valid")
    private Integer isValid;

    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;
}
