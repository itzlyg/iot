package cn.sinozg.applet.biz.oss.sevice.impl;

import cn.sinozg.applet.biz.oss.sevice.OssService;
import cn.sinozg.applet.common.properties.OssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 默认
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-05-16 13:35
 */
@Service
@ConditionalOnSingleCandidate(value = OssService.class)
public class OssServiceImpl implements OssService {
    @Override
    public boolean simpleUpload(OssProperties oss, String key, String mediaType, InputStream is) {
        return false;
    }

    @Override
    public boolean simpleUpload(OssProperties oss, String key, File file) {
        return false;
    }

    @Override
    public void deleteFile(OssProperties oss, List<String> ids) {

    }

    @Override
    public String upToken(OssProperties oss) {
        return "";
    }
}
