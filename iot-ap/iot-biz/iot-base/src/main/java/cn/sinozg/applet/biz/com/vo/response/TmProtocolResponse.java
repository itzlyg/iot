package cn.sinozg.applet.biz.com.vo.response;

import cn.sinozg.applet.biz.com.model.TmBaseDataTypeInfo;
import cn.sinozg.applet.biz.com.model.TmServiceOutInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 物模型返回消息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-08 22:19
 */
@Data
public class TmProtocolResponse {

    @Schema(description = "id")
    private String id;

    @Schema(description = "产品key")
    private String prodKey;

    @Schema(description = "属性定义")
    private List<TmBaseDataTypeInfo> attributes;

    @Schema(description = "服务定义")
    private List<TmServiceOutInfo> service;
}
