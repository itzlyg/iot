package cn.sinozg.applet.biz.oss.service.impl;

import cn.sinozg.applet.biz.oss.sevice.OssService;
import cn.sinozg.applet.biz.oss.vo.model.FileUploadInfo;
import cn.sinozg.applet.biz.oss.vo.response.ChkFileResponse;
import cn.sinozg.applet.biz.oss.vo.response.UploadUrlsResponse;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.function.FunctionException;
import cn.sinozg.applet.common.properties.OssProperties;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-09 17:44
 */
@Slf4j
@Service
public class OssQiNiuServiceImpl implements OssService {

    @Override
    public boolean simpleUpload(OssProperties oss, String key, String mediaType, InputStream is) {
        return upload(oss, key, is);
    }

    @Override
    public boolean simpleUpload(OssProperties oss, String key, File file) {
        return upload(oss, key, file);
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
        Configuration cfg = new Configuration(Region.autoRegion());
        Auth auth = Auth.create(oss.getAccessKey(), oss.getAccessSecret());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        execute(bucketManager, m -> m.delete(bucketName, id));
    }

    @Override
    public void deleteFiles(OssProperties oss, Map<String, List<String>> map) {
        Configuration cfg = new Configuration(Region.autoRegion());
        Auth auth = Auth.create(oss.getAccessKey(), oss.getAccessSecret());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        for (Map.Entry<String, List<String>> e : map.entrySet()) {
            BucketManager.BatchOperations operations = new BucketManager.BatchOperations();
            // 单次文件不超过1000
            List<String> ids = e.getValue();
            operations.addDeleteOp(oss.getBucketName(), PojoUtil.toArray(ids, String.class));
            BatchStatus[] statuses = execute(bucketManager, m -> m.batch(operations), r -> r.jsonToObject(BatchStatus[].class));
            for (int i = 0; i < ids.size(); i++) {
                if (statuses[i].code != HttpStatus.OK.value()) {
                    log.error("文件{}删除失败！", ids.get(i));
                }
            }
        }
    }


    @Override
    public String upToken(OssProperties oos) {
        String key = String.format(RedisKey.OSS_TOKEN, oos.getBucketName());
        String token = RedisUtil.getCacheObject(key);
        if (StringUtils.isBlank(token)) {
            Auth auth = Auth.create(oos.getAccessKey(), oos.getAccessSecret());
            token = auth.uploadToken(oos.getBucketName());
            RedisUtil.setCacheObject(key, token, Duration.ofSeconds(3600));
        }
        return token;
    }

    /**
     * 上传文件
     * @param oss 配置
     * @param key key
     * @param data 数据
     * @return 是否成功
     */
    private boolean upload(OssProperties oss, String key, Object data){
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String upToken = upToken(oss);
        FunctionException<UploadManager, Response> fun;
        if (data instanceof File) {
            File file = PojoUtil.cast(data);
            fun = m -> m.put(file, key, upToken);
        } else {
            InputStream is = PojoUtil.cast(data);
            fun = m -> m.put(is, key, upToken,null, null);
        }
        return execute(uploadManager, fun, r -> {
            DefaultPutRet putRet = JsonUtil.toPojo(r.bodyString(), DefaultPutRet.class);
            return StringUtils.isNotBlank(putRet.key);
        });
    }

    /**
     * 操作文件oss
     * @param manager manager
     * @param function 函数
     * @return 返回结果
     * @param <T> 类型
     */
    private <T> Response execute (T manager, FunctionException<T, Response> function){
        return execute(manager,function,f -> f);
    }

    /**
     * 操作文件oss
     * @param manager manager
     * @param function 函数
     * @param resFun 转换
     * @return 返回结果
     * @param <T> 类型
     * @param <R> 类型
     */
    private <T, R> R execute (T manager, FunctionException<T, Response> function, FunctionException<Response, R> resFun){
        try {
            Response response = function.apply(manager);
            return resFun.apply(response);
        } catch (Exception e) {
            log.error("操作文件到云服务器失败 ", e);
            throw new CavException("操作文件到oss失败！");
        }
    }
}
