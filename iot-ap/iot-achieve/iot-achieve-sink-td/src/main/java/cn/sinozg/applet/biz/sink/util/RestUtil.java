package cn.sinozg.applet.biz.sink.util;

import cn.sinozg.applet.biz.sink.common.TdContext;
import cn.sinozg.applet.biz.sink.model.request.TdRestParams;
import cn.sinozg.applet.biz.sink.model.response.TdRestResponse;
import cn.sinozg.applet.common.config.SystemConfig;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.properties.TdDsProperties;
import cn.sinozg.applet.common.utils.HttpUtil;
import cn.sinozg.applet.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * td rest请求
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-06 15:51:12
 */
@Slf4j
public class RestUtil {
    private static TdRestParams params;


    /**
     * rest 请求 时序数据库
     * @param sql sql
     * @param error 错误信息
     * @return 返回结果
     */
    public static TdRestResponse executeSql(String sql, String error) {
        log.info("执行的TDengine sql 语句为 :{}", sql);
        TdRestParams p = getParams();
        Map<String, String> header = new HashMap<>(64);
        header.put(HttpHeaders.AUTHORIZATION, p.getAuth());
        String json = HttpUtil.doPostJson(p.getUrl(), header, sql);
        if (StringUtils.isBlank(json)) {
            throw new CavException("请求失败！");
        }
        TdRestResponse response = JsonUtil.toPojo(json, TdRestResponse.class);
        if (response == null || TdContext.RUST_CODE_SUCCESS != response.getCode()) {
            log.error(error + "，{}", json);
            throw new CavException(error);
        } else {
            log.info("成功，返回结果为：{}", json);
        }
        return response;
    }

    /**
     * 获取参数
     * @return 参数
     */
    private static TdRestParams getParams (){
        if (params == null) {
            TdDsProperties properties = SystemConfig.APP.getTaos();
            TdRestParams p = new TdRestParams();
            p.setUrl(properties.getRestUrl());
            String auth = properties.getDatasource().getUsername() + BaseConstants.COLON + properties.getDatasource().getPassword();
            auth = "Basic " + Base64.encodeBase64String(auth.getBytes(StandardCharsets.UTF_8));
            p.setAuth(auth);
            params = p;
        }
        return params;
    }
}
