package cn.sinozg.applet.biz.oss.helper;

import cn.sinozg.applet.biz.oss.vo.model.FileUploadInfo;
import cn.sinozg.applet.biz.oss.vo.response.UploadUrlsResponse;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.function.FunctionException;
import cn.sinozg.applet.common.properties.AppValue;
import cn.sinozg.applet.common.properties.OssProperties;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.SnowFlake;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListPartsResponse;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.http.Method;
import io.minio.messages.Part;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * 文件上传
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-23 14:47
 */
@Slf4j
public class MinioHelper {

    @Resource
    private AppValue app;

    @Resource
    private MinioClient minioClient;

    public MinioHelper(){
    }

    /**
     * 获取 Minio 中已经上传的分片文件
     * @param object 文件名称
     * @param uploadId 上传的文件id（由 minio 生成）
     * @return List<Integer>
     */
    public List<Integer> getListParts(String object, String uploadId) {
        List<Part> parts = getParts(object, uploadId);
        return PojoUtil.toList(parts, Part::partNumber);
    }

    /**
     * 单文件签名上传
     * @param object 文件名称（uuid 格式）
     * @return UploadUrlsVO
     */
    public UploadUrlsResponse getUploadObjectUrl(String contentType, String object) {
        log.info("<{}> 开始单文件上传<minio>", object);
        UploadUrlsResponse urls = new UploadUrlsResponse();
        String uploadId = SnowFlake.genId();
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);
        List<String> urlList = getPresObjectUrl(reqParams, contentType, object, 1, null);
        urls.setUploadId(uploadId);
        urls.setUrls(urlList);
        return urls;
    }

    /**
     * 初始化分片上传
     * @param fileUploadInfo 前端传入的文件信息
     * @param object object
     * @return UploadUrlsVO
     */
    public UploadUrlsResponse initMultiPartUpload(FileUploadInfo fileUploadInfo, String object) {
        Integer chunkCount = fileUploadInfo.getChunkCount();
        String contentType = fileUploadInfo.getContentType();
        String uploadId = fileUploadInfo.getUploadId();
        // 如果初始化时有 uploadId，说明是断点续传，不能重新生成 uploadId
        if (StringUtils.isBlank(uploadId)) {
            uploadId = AsyncFactory.instance().initMultiPartUpload(app.getOss().getBucketName(), null, object, headers(contentType), null);
        }
        log.info("文件<{}> - 分片<{}> 初始化分片上传数据 请求头 {}", object, chunkCount, contentType);
        UploadUrlsResponse response = new UploadUrlsResponse();
        response.setUploadId(uploadId);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("uploadId", uploadId);
        List<String> partList = getPresObjectUrl(queryParams, contentType, object, chunkCount, 1);
        log.info("文件初始化分片成功");
        response.setUrls(partList);
        return response;
    }

    /**
     * 合并文件
     * @param object object
     * @param uploadId uploadUd
     */
    public boolean mergeMultipartUpload(String object, String uploadId) {
        log.info("通过 <{}-{}-{}> 合并<分片上传>数据", object, uploadId, app.getOss().getBucketName());
        // 获取所有分片
        List<Part> partsList = getParts(object, uploadId);
        Part[] parts = new Part[partsList.size()];
        int partNumber = 1;
        for (Part part : partsList) {
            parts[partNumber - 1] = new Part(partNumber, part.etag());
            partNumber++;
        }
        // 合并分片
        AsyncFactory.instance().mergeMultipartUpload(app.getOss().getBucketName(), null, object, uploadId, parts, null, null);
        return true;
    }

    /**
     * 获取文件内容和元信息，该文件不存在会抛异常
     * @param object object
     * @return StatObjectResponse
     */
    public StatObjectResponse statObject(String object) {
        return clientGet(c -> c.statObject(StatObjectArgs.builder()
                .bucket(app.getOss().getBucketName())
                .object(object)
                .build()));
    }

    public GetObjectResponse getObject(String object, Long offset, Long contentLength) {
        return clientGet(c -> c.getObject(GetObjectArgs.builder()
                .bucket(app.getOss().getBucketName())
                .object(object)
                .offset(offset)
                .length(contentLength)
                .build()));
    }

    /**
     * 上传小文件
     * @param is 文件 流
     * @param object key
     * @param bucketName 桶名称
     * @param contentType 类型
     * @return etag
     */
    public String smallUpload(InputStream is, String object, String bucketName, String contentType) {
        try {
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(object)
                    .contentType(contentType)
                    .stream(is, is.available(), -1)
                    .build());
            String etag = response.etag();
            log.info("上传文件成功！{}", etag);
            return etag;
        } catch (Exception e) {
            log.error("上传文件至 minio 错误", e);
            throw new CavException("上传文件至 minio 错误！");
        }
    }


    private List<Part> getParts(String object, String uploadId) {
        int partNumberMarker = 0;
        boolean isTruncated = true;
        List<Part> parts = new ArrayList<>();
        while(isTruncated){
            ListPartsResponse partResult = AsyncFactory.instance().listMultipart(app.getOss().getBucketName(), null, object, 1000, partNumberMarker, uploadId, null, null);
            parts.addAll(partResult.result().partList());
            // 检查是否还有更多分片
            isTruncated = partResult.result().isTruncated();
            if (isTruncated) {
                // 更新partNumberMarker以获取下一页的分片数据
                partNumberMarker = partResult.result().nextPartNumberMarker();
            }
        }
        return parts;
    }

    private List<String> getPresObjectUrl (Map<String, String> queryParams, String contentType, String object, int chunkCount, Integer duration){
        OssProperties minio = app.getOss();
        if (duration == null) {
            duration = minio.getUploadExpiry();
        }
        GetPresignedObjectUrlArgs.Builder build = GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(minio.getBucketName())
                .object(object)
                .extraHeaders(headers(contentType))
                .expiry(duration, TimeUnit.DAYS);
        List<String> list = new ArrayList<>();
        try {
            for (int i = 1; i <= chunkCount; i++) {
                if (chunkCount != 1) {
                    queryParams.put("partNumber", String.valueOf(i));
                }
                String url = AsyncFactory.instance().getPresignedObjectUrl(build.extraQueryParams(queryParams).build());
                list.add(url);
            }
        } catch (Exception e) {
            log.error("customMinioClient GET URL {}", e.getMessage());
            throw new CavException("获取文件地址错误！");
        }
        return list;
    }

    private Multimap<String, String> headers(String contentType){
        Multimap<String, String> headers = HashMultimap.create();
        if (StringUtils.isBlank(contentType)) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        headers.put(HttpHeaders.CONTENT_TYPE, contentType);
        return headers;
    }

    private <T> T clientGet (FunctionException<CustomMinioAsyncClient, CompletableFuture<T>> fun) {
        try {
            CompletableFuture<T> future = fun.apply(AsyncFactory.instance());
            return future.get();
        } catch (Exception e) {
            log.error("customMinioClient ERROR {}", e.getMessage());
            throw new CavException("操作文件对象错误！");
        }
    }
}
