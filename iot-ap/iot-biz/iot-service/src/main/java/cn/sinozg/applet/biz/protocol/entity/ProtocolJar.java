package cn.sinozg.applet.biz.protocol.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 协议对应的jar包表
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_iot_protocol_jar")
@Schema(name = "ProtocolJar", description = "协议对应的jar包")
public class ProtocolJar extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** jar名称 */
    @Schema(description = "jar名称")
    @TableField("jar_name")
    private String jarName;


    /** md5 */
    @Schema(description = "md5")
    @TableField("md5")
    private String md5;

}
