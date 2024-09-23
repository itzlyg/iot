package cn.sinozg.applet.biz.oss.vo.response;

import java.util.List;
import lombok.Data;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-19 12:39
 */
@Data
public class UploadUrlsResponse {
    private String uploadId;
    private List<String> urls;
}
