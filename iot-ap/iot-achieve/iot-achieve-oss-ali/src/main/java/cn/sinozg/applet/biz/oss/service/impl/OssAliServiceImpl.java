package cn.sinozg.applet.biz.oss.service.impl;

import cn.sinozg.applet.biz.oss.sevice.OssService;
import cn.sinozg.applet.biz.oss.vo.model.FileUploadInfo;
import cn.sinozg.applet.biz.oss.vo.response.ChkFileResponse;
import cn.sinozg.applet.biz.oss.vo.response.UploadUrlsResponse;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.properties.OssProperties;
import cn.sinozg.applet.common.utils.FileUtil;
import cn.sinozg.applet.common.utils.SnowFlake;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.StorageClass;
import com.aliyun.oss.model.VoidResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 阿里云上传文件
 * @Description
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-05-16 13:17:20
 */
@Slf4j
@Service
public class OssAliServiceImpl implements OssService {
    @Override
    public boolean simpleUpload(OssProperties oss, String key, String mediaType, InputStream is) {
        if (is == null) {
            log.error("通过OSS 流式上传时，输入流为空");
            return false;
        }
        if (StringUtils.isEmpty(key)) {
            key = SnowFlake.genId();
        }
        try  {
            String finalKey = key;
            PutObjectResult result = execute(f -> f.putObject(this.buildRequest(finalKey, mediaType, is, oss)), oss);
            if (result != null) {
                log.info("上传文件成功，md5 值 {}", result.getETag());
            }
        } catch (Exception e) {
            log.error("上传到oss失败！", e);
            throw new CavException("上传到oss失败！");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Override
    public boolean simpleUpload(OssProperties oss, String key, File file) {
        if (file == null) {
            log.error("通过OSS 文件上传，输入文件为空");
            return false;
        }
        String mediaType = FileUtil.fileType(file.getName());
        try {
            return simpleUpload(oss, key, mediaType, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error("获取文件流失败", e);
            throw new CavException("上传到oss失败！");
        }
    }

    @Override
    public ChkFileResponse chkFileByMd5(String md5) {
        return null;
    }

    @Override
    public UploadUrlsResponse initMultipartUpload(FileUploadInfo fileUploadInfo) {
        return null;
    }

    @Override
    public String mergeMultipartUpload(String md5) {
        return "";
    }

    @Override
    public byte[] downloadMultipartFile(String id, HttpServletRequest request, HttpServletResponse response) {
        return new byte[0];
    }

    @Override
    public void deleteFile(OssProperties oss, String bucketName, String id) {
        execute(f -> {
            VoidResult result = f.deleteObject(bucketName, id);
            log.info("删除文件成功，删除文件 请求id {}", result.getRequestId());
            return result.getRequestId();
        }, oss);
    }

    @Override
    public void deleteFiles(OssProperties oss, Map<String, List<String>> map) {
        if (MapUtils.isEmpty(map)) {
            return;
        }
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            execute(f -> {
                DeleteObjectsResult deleteObjectsResult = f.deleteObjects(new DeleteObjectsRequest(entry.getKey()).withKeys(entry.getValue()).withEncodingType("url"));
                List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
                log.info("批量删除文件成功，删除文件数量 {}", deletedObjects.size());
                return deletedObjects.size();
            }, oss);
        }
    }

    @Override
    public String upToken(OssProperties oss) {
        return "";
    }


    /**
     * oss 操作文档
     * @param function 执行器
     * @param prop 配置
     * @return 执行结果
     * @param <R> 类型
     */
    private <R> R execute (Function<OSS, R> function, OssProperties prop){
        OSS ossClient = null;
        try {
            ossClient = this.buildOss(prop);
            return function.apply(ossClient);
        } catch (Exception e) {
            log.error("操作文件到云服务器失败 ", e);
            throw new CavException("操作文件到oss失败！");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 构建请求对象
     * @param keyName 上传文件名
     * @param mediaType 文件类型
     * @param inputStream 文件流
     * @param prop 配置
     * @return 请求对象
     */
    private PutObjectRequest buildRequest(String keyName, String mediaType, InputStream inputStream, OssProperties prop) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        metadata.setObjectAcl(CannedAccessControlList.PublicRead);
        // 设置上传内容类型
        metadata.setContentType(mediaType);
        // 被下载时网页的缓存行为
        metadata.setCacheControl("no-cache");
        PutObjectRequest request = new PutObjectRequest(prop.getBucketName(), keyName, inputStream);
        request.setMetadata(metadata);
        return request;
    }


    /**
     * 构建oss 组件
     * @param prop 配置
     * @return 组件
     */
    private OSS buildOss(OssProperties prop) {
        return new OSSClientBuilder().build(
                prop.getEndPoint(), prop.getAccessKey(), prop.getAccessSecret());
    }
}
