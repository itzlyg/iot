package cn.sinozg.applet.biz.system.vo.response;

import cn.sinozg.applet.common.annotation.PicUrl;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件返回信息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-09 14:29
 */
@Data
public class FileInfoResponse {
    /**
     * 图片Id
     */
    @JsonProperty("id")
    @Schema(description = "图片Id")
    private String id;
    /**
     * 图片地址
     */
    @PicUrl
    @JsonProperty("pic")
    @Schema(description = "图片地址")
    private String pic;

    /**
     * 业务Id
     */
    @JsonProperty("bizId")
    @Schema(description = "业务Id")
    private String bizId;

    /** 文件名称 */
    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件大小")
    private Long fileSize;

    /** 文件类型 */
    @Schema(description = "文件类型")
    private String mediaType;

}
