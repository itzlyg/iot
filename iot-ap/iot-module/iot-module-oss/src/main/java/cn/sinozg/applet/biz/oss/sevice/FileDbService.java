package cn.sinozg.applet.biz.oss.sevice;

import cn.sinozg.applet.biz.oss.vo.model.FileIdInfo;
import cn.sinozg.applet.biz.oss.vo.model.FileUploadInfo;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-23 15:22
 */
public interface FileDbService {

    /**
     * 数据库查询文件
     * @param md5 md5
     * @return 文件信息
     */
    FileUploadInfo fileByMd5(String md5);

    /**
     * 保存文件
     * @param bucketName 桶名称
     * @param params 参数
     */
    void saveFile(String bucketName, FileUploadInfo params);

    /**
     * 文件id 查询数据
     * @param id 文件id
     * @return 数据
     */
    FileIdInfo fileIdById(String id);
}
