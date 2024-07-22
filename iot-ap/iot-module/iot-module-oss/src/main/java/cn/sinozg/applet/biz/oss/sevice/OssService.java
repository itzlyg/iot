package cn.sinozg.applet.biz.oss.sevice;

import cn.sinozg.applet.common.properties.OssProperties;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 文件存储实现方案
 * 目前是七牛实现，可以自己用阿里云或者minio等实现
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-09 17:39
 */
public interface OssService {

    /**
     * 上传文件到 cdn上
     * @param oss 配置信息
     * @param key key
     * @param mediaType 文件类型
     * @param is 流
     * @return 是否成功
     */
    boolean simpleUpload (OssProperties oss, String key, String mediaType, InputStream is);

    /**
     * 上传文件到 cdn上
     * @param oss 配置信息
     * @param key key
     * @param file 文件
     * @return 是否成功
     */
    boolean simpleUpload (OssProperties oss, String key, File file);

    /**
     * 批量删除文件
     * @param oss 配置信息
     * @param ids 文件id集合
     */
    void deleteFile (OssProperties oss, List<String> ids);

    /**
     * 获取到token
     * @param oss 配置信息
     * @return token
     */
    String upToken (OssProperties oss);
}
