package cn.sinozg.applet.common.properties;

import lombok.Data;


/**
 * @Description 文件存储配置
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-08-29 11:45:16
 */
@Data
public class OssProperties {

    /** point */
    private String endPoint;
    /** key */
    private String accessKeyId;
    /** secret */
    private String accessKeySecret;
    /** 桶名称 */
    private String bucketName;
}
