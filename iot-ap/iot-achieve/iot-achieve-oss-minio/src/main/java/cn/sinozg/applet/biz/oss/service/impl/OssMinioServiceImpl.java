package cn.sinozg.applet.biz.oss.service.impl;

import cn.sinozg.applet.biz.oss.enums.UploadStatus;
import cn.sinozg.applet.biz.oss.helper.MinioHelper;
import cn.sinozg.applet.biz.oss.sevice.FileDbService;
import cn.sinozg.applet.biz.oss.sevice.OssService;
import cn.sinozg.applet.biz.oss.vo.model.FileIdInfo;
import cn.sinozg.applet.biz.oss.vo.model.FileUploadInfo;
import cn.sinozg.applet.biz.oss.vo.response.ChkFileResponse;
import cn.sinozg.applet.biz.oss.vo.response.UploadUrlsResponse;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.properties.AppValue;
import cn.sinozg.applet.common.properties.OssProperties;
import cn.sinozg.applet.common.utils.DateUtil;
import cn.sinozg.applet.common.utils.FileUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import cn.sinozg.applet.common.utils.SnowFlake;
import io.minio.GetObjectResponse;
import io.minio.StatObjectResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2024-09-09 17:44
 */
@Slf4j
@Service
public class OssMinioServiceImpl implements OssService {
    /** 64KB */
    private static final Integer BUFFER_SIZE = 1024 * 64;
    @Resource
    private AppValue app;

    @Resource
    private MinioHelper minioHelper;

    @Resource
    private FileDbService fileDbService;

    @Override
    public boolean simpleUpload(OssProperties oss, String key, String mediaType, InputStream is) {
        minioHelper.smallUpload(is, key, oss.getBucketName(), mediaType);
        return true;
    }

    @Override
    public boolean simpleUpload(OssProperties oss, String key, File file) {
        try (InputStream is = Files.newInputStream(file.toPath())) {
            return simpleUpload(oss, key, FileUtil.fileType(file), is);
        } catch (Exception e) {
            log.error("上传文件至 minio 错误", e);
            throw new CavException("上传文件至 minio 错误！");
        }
    }

    @Override
    public ChkFileResponse chkFileByMd5(String md5) {
        log.info("查询md5: <{}> 在redis是否存在", md5);
        FileUploadInfo info = RedisUtil.getCacheObject(md5);
        if (info != null) {
            List<Integer> listParts = minioHelper.getListParts(info.getObjKey(), info.getUploadId());
            info.setListParts(listParts);
            return new ChkFileResponse(info, UploadStatus.UPLOAD_ING);
        }
        log.info("redis中不存在md5: <{}> 查询mysql是否存在", md5);
        FileUploadInfo file = fileDbService.fileByMd5(md5);
        if (file != null) {
            log.info("mysql中存在md5: <{}> 的文件 该文件已上传至minio 秒传直接过", md5);
            FileUploadInfo dbFileInfo = PojoUtil.copyBean(file, FileUploadInfo.class);
            return new ChkFileResponse(dbFileInfo, UploadStatus.UPLOAD_SUC);
        }
        return new ChkFileResponse(null, UploadStatus.UPLOAD_NON);
    }

    @Override
    public UploadUrlsResponse initMultipartUpload(FileUploadInfo params) {
        String md5Key = minioKey(params.getMd5());
        FileUploadInfo redisInfo = RedisUtil.getCacheObject(md5Key);
        // 若 redis 中有该 md5 的记录，以 redis 中为主
        String object;
        if (redisInfo != null) {
            params = redisInfo;
            object = redisInfo.getObjKey();
        } else {
            String originFileName = params.getOrgiFileName();
            String suffix = StringUtils.substringAfterLast(originFileName, Constants.SPOT);
            // 对文件重命名，并以年月日文件夹格式存储
            object = String.format("%s/%s", DateUtil.formatDate(LocalDate.now(),"yyyy/MM/dd"), SnowFlake.genId());
            params.setObjKey(object);
            params.setFileSuffix(suffix);
        }
        UploadUrlsResponse result;
        // 单文件上传
        if (params.getChunkCount() == 1) {
            log.info("当前分片数量 <{}> 单文件上传", params.getChunkCount());
            result = minioHelper.getUploadObjectUrl(params.getContentType(), object);
        } else {
            // 分片上传
            log.info("当前分片数量 <{}> 分片上传", params.getChunkCount());
            result = minioHelper.initMultiPartUpload(params, object);
        }
        params.setUploadId(result.getUploadId());
        // 存入 redis （单片存 redis 唯一用处就是可以让单片也入库，因为单片只有一个请求，基本不会出现问题）
        RedisUtil.setCacheObject(md5Key, params, Duration.ofDays(1));
        return result;
    }

    @Override
    public String mergeMultipartUpload(String md5) {
        String md5Key = minioKey(md5);
        FileUploadInfo info = RedisUtil.getCacheObject(md5Key);
        OssProperties minio = app.getOss();
        String url = String.format("%s/%s/%s", minio.getEndPoint(), minio.getBucketName(), info.getObjKey());
        Integer chunkCount = info.getChunkCount();
        // 分片为 1 ，不需要合并，否则合并后看返回的是 true 还是 false
        boolean isSuccess = chunkCount == 1 || minioHelper.mergeMultipartUpload(info.getObjKey(), info.getUploadId());
        if (!isSuccess) {
            throw new CavException(UploadStatus.UPLOAD_FAILED.getMsg());
        }
        fileDbService.saveFile(minio.getBucketName(), info);
        RedisUtil.deleteKeys(md5Key);
        return url;
    }

    @Override
    public byte[] downloadMultipartFile(String id, HttpServletRequest request, HttpServletResponse response) {
        // redis 缓存当前文件信息，避免分片下载时频繁查库
        String minoKey = String.format(RedisKey.OSS_ID, id);
        FileIdInfo fileId = RedisUtil.getCacheObject(minoKey);
        if (fileId == null) {
            fileId = fileDbService.fileIdById(id);
            if (fileId == null) {
                throw new CavException("文件不存在！");
            }
            RedisUtil.setCacheObject(minoKey, fileId, Duration.ofDays(1));
        }
        String range = request.getHeader(HttpHeaders.RANGE);
        String fileName = fileId.getOrgiFileName();
        // 获取 bucket 桶中的文件元信息，获取不到会抛出异常
        StatObjectResponse stat = minioHelper.statObject(fileId.getObjKey());
        long fileSize = stat.size();
        // 开始下载位置  结束下载位置
        ImmutablePair<Long, Long> rangePair = rangInfo(range, fileSize);
        long startByte = rangePair.getKey();
        long endByte = rangePair.getValue();
        // 要下载的长度 确保返回的 contentLength 不会超过文件的实际剩余大小
        long contentLength = Math.min(endByte - startByte + 1, fileSize - startByte);
        // 文件类型 解决下载文件时文件名乱码问题
        byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        fileName = new String(fileNameBytes, StandardCharsets.ISO_8859_1);

        // 响应头设置---------------------------------------------------------------------------------------------
        // 断点续传，获取部分字节内容：
        response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        // http状态码要为206：表示获取部分内容,SC_PARTIAL_CONTENT,若部分浏览器不支持，改成 SC_OK
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setHeader(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name());
        response.setHeader(HttpHeaders.LAST_MODIFIED, stat.lastModified().toString());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        // Content-Range，格式为：[要下载的开始位置]-[结束位置]/[文件总大小]
        response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + startByte + "-" + endByte + "/" + fileSize);
        response.setHeader(HttpHeaders.ETAG, "\"".concat(stat.etag()).concat("\""));
        try (BufferedOutputStream os = new BufferedOutputStream(response.getOutputStream());
             GetObjectResponse stream = minioHelper.getObject(stat.object(), startByte, contentLength)){
            // 将读取的文件写入到 OutputStream
            byte[] bytes = new byte[BUFFER_SIZE];
            long bytesWritten = 0;
            int bytesRead;
            while ((bytesRead = stream.read(bytes)) != -1) {
                if (bytesWritten + bytesRead >= contentLength) {
                    os.write(bytes, 0, (int)(contentLength - bytesWritten));
                    break;
                } else {
                    os.write(bytes, 0, bytesRead);
                    bytesWritten += bytesRead;
                }
            }
            os.flush();
            response.flushBuffer();
            // 返回对应http状态
            return bytes;
        } catch (Exception e) {
            log.error("上传文件失败！{}", e.getMessage());
            throw new CavException("上传文件失败！");
        }
    }


    @Override
    public void deleteFile(OssProperties oos, List<String> ids) {

    }

    @Override
    public String upToken(OssProperties oos) {
        return null;
    }

    /**
     * 获取到范围
     * @param range range
     * @param fileSize 大小
     * @return 开始和结束
     */
    private ImmutablePair<Long, Long> rangInfo(String range, long fileSize){
        // 开始下载位置
        long startByte = 0;
        // 结束下载位置
        long endByte = fileSize - 1;
        // 存在 range，需要根据前端下载长度进行下载，即分段下载 例如：range=bytes=0-52428800
        if (StringUtils.isNotBlank(range) && StringUtils.containsAny(range, "bytes=", Constants.MIDDLE_LINE)) {
            // 0-52428800
            range = StringUtils.substringAfterLast(range, "=").trim();
            String[] ranges = range.split(Constants.MIDDLE_LINE);
            // 类型三：bytes=22-2343
            if (ranges.length == 2) {
                startByte = Long.parseLong(ranges[0]);
                endByte = Long.parseLong(ranges[1]);
            } else if (ranges.length == 1) {
                // 类型一：bytes=-2343 后端转换为 0-2343
                if (range.startsWith(Constants.MIDDLE_LINE)) {
                    endByte = Long.parseLong(ranges[0]);
                }
                // 类型二：bytes=2343- 后端转换为 2343-最后
                if (range.endsWith(Constants.MIDDLE_LINE)) {
                    startByte = Long.parseLong(ranges[0]);
                }
            }
        }
        return ImmutablePair.of(startByte, endByte);
    }


    private String minioKey (String md5){
        return String.format(RedisKey.OSS_MD5, md5);
    }

}
