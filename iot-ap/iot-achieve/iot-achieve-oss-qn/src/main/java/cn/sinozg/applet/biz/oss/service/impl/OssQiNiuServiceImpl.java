package cn.sinozg.applet.biz.oss.service.impl;

import cn.sinozg.applet.biz.oss.sevice.OssService;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.properties.OssProperties;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;

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
    public boolean simpleUpload(OssProperties oss, String key, InputStream is) {
        return upload(oss, key, is);
    }

    @Override
    public boolean simpleUpload(OssProperties oss, String key, File file) {
        return upload(oss, key, file);
    }


    @Override
    public void deleteFile(OssProperties oos, List<String> ids) {
        Configuration cfg = new Configuration(Region.autoRegion());
        Auth auth = Auth.create(oos.getAccessKeyId(), oos.getAccessKeySecret());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        BucketManager.BatchOperations operations = new BucketManager.BatchOperations();
        // 单次文件不超过1000
        operations.addDeleteOp(oos.getBucketName(), PojoUtil.toArray(ids, String.class));
        try {
            Response response = bucketManager.batch(operations);
            BatchStatus[] statuses = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < ids.size(); i++) {
                if (statuses[i].code != HttpStatus.OK.value()) {
                    log.error("文件{}删除失败！", ids.get(i));
                }
            }
        } catch (QiniuException e) {
            throw new CavException("批量删除文件失败！", e);
        }
    }

    @Override
    public String upToken(OssProperties oos) {
        String key = String.format(RedisKey.OSS_TOKEN, oos.getBucketName());
        String token = RedisUtil.getCacheObject(key);
        if (StringUtils.isBlank(token)) {
            Auth auth = Auth.create(oos.getAccessKeyId(), oos.getAccessKeySecret());
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
        boolean result;
        String upToken = upToken(oss);
        try {
            Response response;
            if (data instanceof File) {
                File file = PojoUtil.cast(data);
                response = uploadManager.put(file, key, upToken);
            } else {
                InputStream is = PojoUtil.cast(data);
                response = uploadManager.put(is, key, upToken,null, null);
            }
            //解析上传成功的结果
            DefaultPutRet putRet = JsonUtil.toPojo(response.bodyString(), DefaultPutRet.class);
            result = StringUtils.isNotBlank(putRet.key);
        } catch (Exception ex) {
            log.error("上传七牛错误，{}", ex.getMessage());
            throw new CavException("上传文件失败！");
        }
        return result;
    }
}
