package cn.sinozg.applet.biz.system.service.impl;

import cn.sinozg.applet.biz.oss.sevice.OssService;
import cn.sinozg.applet.biz.system.entity.FileMapping;
import cn.sinozg.applet.biz.system.mapper.FileMappingMapper;
import cn.sinozg.applet.biz.system.service.FileMappingService;
import cn.sinozg.applet.biz.system.vo.request.DeleteFileRequest;
import cn.sinozg.applet.biz.system.vo.request.LargeFileRequest;
import cn.sinozg.applet.biz.system.vo.response.FileInfoResponse;
import cn.sinozg.applet.biz.system.vo.response.FileUploadTempResponse;
import cn.sinozg.applet.biz.system.vo.response.LargeFileResponse;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.properties.AppValue;
import cn.sinozg.applet.common.utils.FileUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import cn.sinozg.applet.common.utils.SnowFlake;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
* 文件映射表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-08-29 14:15:26
*/
@Service
public class FileMappingServiceImpl extends ServiceImpl<FileMappingMapper, FileMapping> implements FileMappingService {

    private static final int M_10 = 10 * 1024 * 1024;
    @Resource
    private FileMappingMapper fileMapper;

    @Resource
    private OssService oss;

    @Resource
    private AppValue app;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileInfoResponse addFile(HttpServletRequest request) {
        FileUploadTempResponse fileInfo = fileInfo(request);
        MultipartFile f = fileInfo.getFile();
        // 10M
        if (f.getSize() > M_10) {
            throw new CavException("文件超过10M！");
        }
        String bizId = StringUtils.defaultIfBlank(fileInfo.getBizId(), SnowFlake.genId());
        FileMapping mapping = mapping(f.getOriginalFilename(), bizId, f.getSize());
        FileInfoResponse pic = toResponse(mapping);
        try (InputStream is = f.getInputStream()) {
            // 上传文件到oss
            boolean success = oss.simpleUpload(app.getOss(), mapping.getId(), mapping.getMediaType(), is);
            if (!success) {
                throw new CavException("上传文件失败！");
            }
            fileMapper.insert(mapping);
        } catch (Exception e) {
            log.error("上传文件失败！：", e);
            if (e instanceof CavException) {
                throw (CavException) e;
            }
            throw new CavException("文件上传失败！");
        }
        return pic;
    }

    /**
     * 文件上传前获取到信息
     * @param params 文件参数
     * @return 上传的必要信息
     */
    @Override
    public LargeFileResponse largeFileUpload(LargeFileRequest params) {
        FileMapping mapping = mapping(params.getFileName(), params.getBizId(), params.getFileSize());
        LargeFileResponse response = new LargeFileResponse();
        String id = mapping.getId();
        response.setId(id);
        response.setToken(oss.upToken(app.getOss()));
        RedisUtil.setCacheObject(String.format(RedisKey.LARGE_FILE, id), mapping, Duration.ofMinutes(30));
        return response;
    }

    @Override
    public FileInfoResponse saveLargeFile (String id){
        String key = String.format(RedisKey.LARGE_FILE, id);
        FileMapping mapping = RedisUtil.getCacheObject(key);
        if (mapping == null) {
            throw new CavException("文件信息过期，请重新上传！");
        }
        this.save(mapping);
        RedisUtil.deleteObject(key);
        return toResponse(mapping);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean deleteFile(DeleteFileRequest params) {
        if (StringUtils.isBlank(params.getBizId()) && CollectionUtils.isEmpty(params.getIds())) {
            throw new CavException("请至少传入一个参数！");
        }
        LambdaQueryWrapper<FileMapping> query = new LambdaQueryWrapper<FileMapping>()
                .eq(StringUtils.isNotBlank(params.getBizId()), FileMapping::getBizId, params.getBizId());
        if (CollectionUtils.isNotEmpty(params.getIds())) {
            query.in(FileMapping::getId, params.getIds());
        }
        List<FileMapping> list = this.list(query);
        if (CollectionUtils.isEmpty(list)) {
            throw new CavException("文件不存在！");
        }
        List<String> ids = PojoUtil.toList(list, FileMapping::getId);
        PojoUtil.executeBatch(ids, c -> {
            oss.deleteFile(app.getOss(), c);
            this.removeBatchByIds(c);
        });
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean deleteFile(String fileId) {
        if (StringUtils.isBlank(fileId)) {
            return false;
        }
        DeleteFileRequest params = new DeleteFileRequest();
        params.setIds(Collections.singletonList(fileId));
        return deleteFile(params);
    }

    @Override
    public FileUploadTempResponse fileInfo (HttpServletRequest request){
        MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
        String bizId = StringUtils.trimToNull(mrequest.getParameter("bizId"));
        List<MultipartFile> files = mrequest.getFiles("uploadFiles");
        if (CollectionUtils.isEmpty(files) || (CollectionUtils.isNotEmpty(files) && files.size() != 1)) {
            throw new CavException("只能一次上传一个文件！");
        }
        FileUploadTempResponse file = new FileUploadTempResponse();
        file.setFile(files.get(0));
        file.setBizId(bizId);
        return file;
    }

    /**
     * 文件信息返回
     * @param mapping 返回保存后的文件信息
     * @return 文件信息
     */
    private FileInfoResponse toResponse (FileMapping mapping){
        FileInfoResponse pic = new FileInfoResponse();
        pic.setId(mapping.getId());
        pic.setBizId(mapping.getBizId());
        pic.setFileName(mapping.getFileName());
        pic.setMediaType(mapping.getMediaType());
        pic.setPic(mapping.getId());
        pic.setFileSize(mapping.getFileSize());
        return pic;
    }
    /**
     * 封装 fileMapper
     * @param fileName 文件名称
     * @param bizId 业务id
     * @param size size
     * @return 文件实体
     */
    private FileMapping mapping(String fileName, String bizId, long size){
        String id = SnowFlake.genId();
        String mediaType = FileUtil.fileType(fileName);

        FileMapping mapping = new FileMapping();
        mapping.setId(id);
        mapping.setBizId(bizId);
        mapping.setFileName(fileName);
        mapping.setFileSuffix(StringUtils.substringAfterLast(fileName, Constants.SPOT));
        mapping.setMediaType(mediaType);

        mapping.setFileSize(size);
        mapping.setBucketName(app.getOss().getBucketName());
        return mapping;
    }

}
