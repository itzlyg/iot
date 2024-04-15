package cn.sinozg.applet.biz.system.service;

import cn.sinozg.applet.biz.system.entity.FileMapping;
import cn.sinozg.applet.biz.system.vo.request.DeleteFileRequest;
import cn.sinozg.applet.biz.system.vo.request.LargeFileRequest;
import cn.sinozg.applet.biz.system.vo.response.FileInfoResponse;
import cn.sinozg.applet.biz.system.vo.response.FileUploadTempResponse;
import cn.sinozg.applet.biz.system.vo.response.LargeFileResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* 文件映射表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-08-29 14:15:26
*/
public interface FileMappingService extends IService<FileMapping> {

    /**
     * 文件上传
     * @param request request
     * @return 返回信息
     */
    FileInfoResponse addFile(HttpServletRequest request);

    /**
     * 解析文件
     * @param request request
     * @return 返回信息
     */
    FileUploadTempResponse fileInfo (HttpServletRequest request);

    /**
     * 删除文件
     * @param params 请求参数
     * @return 是否成功
     */
    boolean deleteFile(DeleteFileRequest params);

    /**
     * 根据id 删除文件信息
     * @param fileId 文件id
     * @return 是否成功
     */
    boolean deleteFile (String fileId);

    /**
     * 大文件上传前获取信息
     * @param params 参数
     * @return 返回信息
     */
    LargeFileResponse largeFileUpload(LargeFileRequest params);

    /**
     * 保存大文件存储信息
     * @param id id
     * @return FileInfoResponse
     */
    FileInfoResponse saveLargeFile (String id);
}
