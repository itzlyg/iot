package cn.sinozg.applet.biz.system.entity;

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
* 租户消息表表
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-09 16:04:37
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_corp_info")
@Schema(name = "CorpInfo", description = "租户消息表")
public class CorpInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 租户id */
    @Schema(description = "租户id")
    @TableField(value = "tenant_id", updateStrategy = FieldStrategy.NEVER)
    private String tenantId;


    /** 租户名称 */
    @Schema(description = "租户名称")
    @TableField("corp_name")
    private String corpName;


    /** 联系人 */
    @Schema(description = "联系人")
    @TableField("liaison")
    private String liaison;


    /** 联系电话 */
    @Schema(description = "联系电话")
    @TableField("phone_num")
    private String phoneNum;


    /** 邮箱地址 */
    @Schema(description = "邮箱地址")
    @TableField("email")
    private String email;


    /** 联系地址 */
    @Schema(description = "联系地址")
    @TableField("address")
    private String address;


    /** 数据状态;00 未启用 01 启用 */
    @Schema(description = "数据状态;00 未启用 01 启用")
    @TableField("data_status")
    private String dataStatus;

}
