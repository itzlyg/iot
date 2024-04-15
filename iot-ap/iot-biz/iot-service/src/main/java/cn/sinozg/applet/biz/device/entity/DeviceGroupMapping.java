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
* 设备分组关联
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-12-15 15:19:25
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_set_device_group_mapping")
@Schema(name = "DeviceGroupMapping", description = "设备分组关联")
public class DeviceGroupMapping extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 组主键 */
    @Schema(description = "组主键")
    @TableField("group_id")
    private String groupId;


    /** 设备主键 */
    @Schema(description = "设备主键")
    @TableField("device_id")
    private String deviceId;


    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;

}
