package cn.sinozg.applet.biz.oss.helper;

import cn.sinozg.applet.common.utils.SpringUtil;
import io.minio.MinioAsyncClient;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-23 14:58
 */
public class AsyncFactory {
    private volatile static CustomMinioAsyncClient CLIENT;

    private AsyncFactory() {}

    /**
     * 获取到实例
     * @return 实例对象
     */
    public static CustomMinioAsyncClient instance(){
        if(CLIENT == null){
            synchronized (CustomMinioAsyncClient.class){
                if(CLIENT == null){
                    CLIENT = new CustomMinioAsyncClient(SpringUtil.getBean(MinioAsyncClient.class));
                }
            }
        }
        return CLIENT;
    }
}
