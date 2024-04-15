package cn.sinozg.applet.biz.system.entity;

import cn.sinozg.applet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 文件映射表表
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-08-29 14:15:26
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file_mapping")
@Schema(name = "FileMapping", description = "文件映射表")
public class FileMapping extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /** 业务id */
    @Schema(description = "业务id")
    @TableField("biz_id")
    private String bizId;


    /** 本地存储 */
    @Schema(description = "本地存储")
    @TableField("local_path")
    private String localPath;


    /** 文件名称 */
    @Schema(description = "文件名称")
    @TableField("file_name")
    private String fileName;


    /** 文件后缀 */
    @Schema(description = "文件后缀")
    @TableField("file_suffix")
    private String fileSuffix;

    /** 文件大小 */
    @Schema(description = "文件大小")
    @TableField("file_size")
    private Long fileSize;

    /** 文件类型 */
    @Schema(description = "文件类型")
    @TableField("media_type")
    private String mediaType;

    /** 存储桶名称 */
    @Schema(description = "存储桶名称")
    @TableField("bucket_name")
    private String bucketName;

}
