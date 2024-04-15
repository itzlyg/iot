package cn.sinozg.applet.biz.system.vo.response;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-20 23:01
 */
@Data
public class FileUploadTempResponse {

    private String bizId;

    private MultipartFile file;
}
