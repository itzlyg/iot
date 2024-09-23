package cn.sinozg.applet.biz.oss.sevice.impl;

import cn.sinozg.applet.biz.oss.sevice.FileDbService;
import cn.sinozg.applet.biz.oss.vo.model.FileIdInfo;
import cn.sinozg.applet.biz.oss.vo.model.FileUploadInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.stereotype.Service;

/**
 * @author xyb
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-09-23 15:46
 */
@Service
@ConditionalOnSingleCandidate(value = FileDbService.class)
public class FileDbServiceImpl implements FileDbService {
    @Override
    public FileUploadInfo fileByMd5(String md5) {
        return null;
    }

    @Override
    public void saveFile(String bucketName, FileUploadInfo params) {

    }

    @Override
    public FileIdInfo fileIdById(String id) {
        return null;
    }
}
