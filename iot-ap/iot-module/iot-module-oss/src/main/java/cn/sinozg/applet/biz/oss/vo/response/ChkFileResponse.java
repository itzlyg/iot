package cn.sinozg.applet.biz.oss.vo.response;

import cn.sinozg.applet.biz.oss.enums.UploadStatus;
import cn.sinozg.applet.biz.oss.vo.model.FileUploadInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-23 14:10
 */
@Data
public class ChkFileResponse {
    @Schema(description = "状态")
    private String status;
    @Schema(description = "文件信息")
    private FileUploadInfo info;

    public ChkFileResponse(){
    }

    public ChkFileResponse(FileUploadInfo info, UploadStatus status){
        this.info = info;
        this.status = status.getCode();
    }
}
