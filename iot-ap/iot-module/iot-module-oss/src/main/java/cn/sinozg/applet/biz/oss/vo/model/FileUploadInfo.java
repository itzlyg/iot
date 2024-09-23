package cn.sinozg.applet.biz.oss.vo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文件上传信息，查询 redis 后的返回信息
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-23 14:06
 */
@Data
public class FileUploadInfo {

    @NotBlank(message = "md5 不能为空")
    @Schema(description = "md5")
    private String md5;

    @Schema(description = "上传id")
    private String uploadId;

    @NotBlank(message = "文件名不能为空")
    @Schema(description = "文件名")
    private String orgiFileName;

    @Schema(description = "仅秒传会有值")
    private String url;

    @Schema(description = "后端使用")
    private String objKey;

    @Schema(description = "后缀")
    private String fileSuffix;

    @NotNull(message = "文件大小不能为空")
    @Schema(description = "文件大小")
    private Long size;

    @NotNull(message = "分片数量不能为空")
    @Schema(description = "分片数量")
    private Integer chunkCount;

    @NotNull(message = "分片大小不能为空")
    @Schema(description = "分片大小")
    private Integer chunkSize;

    @Schema(description = "文件类型")
    private String contentType;

    @Schema(description = "listParts 从 1 开始，前端需要上传的分片索引+1")
    private List<Integer> listParts;
}
