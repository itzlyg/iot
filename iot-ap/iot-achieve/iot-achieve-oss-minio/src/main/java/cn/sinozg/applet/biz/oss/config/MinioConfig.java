package cn.sinozg.applet.biz.oss.config;

import cn.sinozg.applet.common.properties.AppValue;
import cn.sinozg.applet.common.properties.OssProperties;
import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * minio 配置
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-23 14:00
 */
@Component
public class MinioConfig {
    @Resource
    private AppValue app;
    @Bean
    public MinioClient minioClient() {
        OssProperties oss = app.getOss();
        return MinioClient.builder()
                .endpoint(oss.getEndPoint())
                .credentials(oss.getAccessKey(), oss.getAccessSecret())
                .build();
    }

    @Bean
    public MinioAsyncClient minioAsyncClient(){
        OssProperties oss = app.getOss();
        return MinioAsyncClient.builder()
                .endpoint(oss.getEndPoint())
                .credentials(oss.getAccessKey(), oss.getAccessSecret())
                .build();
    }
}
