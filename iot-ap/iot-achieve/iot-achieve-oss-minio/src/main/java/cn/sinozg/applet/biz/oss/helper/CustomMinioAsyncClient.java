package cn.sinozg.applet.biz.oss.helper;

import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.function.SupplierException;
import com.google.common.collect.Multimap;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.messages.Part;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-23 14:16
 */
@Slf4j
public class CustomMinioAsyncClient extends MinioAsyncClient {

    public CustomMinioAsyncClient(MinioAsyncClient client) {
        super(client);
    }


    /**
     * 初始化分片上传、获取 uploadId
     * @param bucket String  存储桶名称
     * @param region String
     * @param object String   文件名称
     * @param headers Multimap<String, String> 请求头
     * @param extraQueryParams Multimap<String, String>
     * @return String
     */
    public String initMultiPartUpload(String bucket, String region, String object, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) {
        CreateMultipartUploadResponse response = s3Async(() -> super.createMultipartUploadAsync(bucket, region, object, headers, extraQueryParams));
        return response.result().uploadId();
    }

    /**
     * 合并分片
     * @param bucketName String   桶名称
     * @param region String
     * @param objectName String   文件名称
     * @param uploadId String   上传的 uploadId
     * @param parts Part[]   分片集合
     * @param extraHeaders Multimap<String, String>
     * @param extraQueryParams Multimap<String, String>
     * @return ObjectWriteResponse
     */
    public ObjectWriteResponse mergeMultipartUpload(String bucketName, String region, String objectName, String uploadId, Part[] parts, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) {
        return s3Async(() -> super.completeMultipartUploadAsync(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams));
    }

    /**
     * 查询当前上传后的分片信息
     * @param bucketName String   桶名称
     * @param region String
     * @param objectName String   文件名称
     * @param maxParts Integer  分片数量
     * @param partNumberMarker Integer  分片起始值
     * @param uploadId String   上传的 uploadId
     * @param extraHeaders Multimap<String, String>
     * @param extraQueryParams Multimap<String, String>
     * @return ListPartsResponse
     */
    public ListPartsResponse listMultipart(String bucketName, String region, String objectName, Integer maxParts, Integer partNumberMarker, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) {
        return s3Async(() -> super.listPartsAsync(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams));
    }

    /**
     * 异步操作文件
     * @param sup 函数
     * @return 结果
     * @param <T> 结果类型
     */
    private <T> T s3Async (SupplierException<CompletableFuture<T>> sup){
        try {
            CompletableFuture<T> future = sup.get();
            return future.get();
        } catch (Exception e) {
            log.error("异步处理文件错误！{}", e.getMessage());
            throw new CavException("异步操作文件错误！");
        }
    }
}
