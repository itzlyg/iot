package cn.sinozg.applet.biz.oss.enums;

import lombok.Getter;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-23 14:10
 */
@Getter
public enum UploadStatus {
    UPLOAD_NON ("01", "未上传"),
    UPLOAD_ING ("02", "上传中"),
    UPLOAD_SUC ("03", "上传成功"),
    UPLOAD_FAILED ("04", "上传失败"),
            ;
    private final String code;
    private final String msg;
    UploadStatus(String code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }
}
