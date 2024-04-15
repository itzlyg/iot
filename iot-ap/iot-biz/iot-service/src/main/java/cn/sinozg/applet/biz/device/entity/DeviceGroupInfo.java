package cn.sinozg.applet.biz.device.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 设备分组信息表表
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_set_device_group_info")
@Schema(name = "DeviceGroupInfo", description = "设备分组信息表")
public class DeviceGroupInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 分组名称 */
    @Schema(description = "分组名称")
    @TableField("group_name")
    private String groupName;


    /** 分组归属用户 */
    @Schema(description = "分组归属用户")
    @TableField("group_owner")
    private String groupOwner;


    /** 分组说明 */
    @Schema(description = "分组说明")
    @TableField("group_desc")
    private String groupDesc;

    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private String tenantId;


    /** 顺序号 */
    @Schema(description = "顺序号")
    @TableField("seq_no")
    private Integer seqNo;


    /** 是否有效0无效1有效 */
    @Schema(description = "是否有效0无效1有效")
    @TableField("is_valid")
    private Integer isValid;

}
