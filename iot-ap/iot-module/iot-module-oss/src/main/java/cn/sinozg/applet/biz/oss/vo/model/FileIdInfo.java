package cn.sinozg.applet.biz.oss.vo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-23 14:08
 */
@Data
public class FileIdInfo {
    @Schema(description = "主键")
    private String id;

    @Schema(description = "原始文件名称")
    private String orgiFileName;

    @Schema(description = "mino key")
    private String objKey;

}
