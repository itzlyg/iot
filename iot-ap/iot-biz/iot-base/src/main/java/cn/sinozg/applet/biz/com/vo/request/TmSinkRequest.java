package cn.sinozg.applet.biz.com.vo.request;

import cn.sinozg.applet.biz.com.model.TmBaseDataTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 物模型写入到数据库
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-07 22:19
 */
@Data
public class TmSinkRequest {

    @Schema(description = "产品key")
    private String prodKey;

    @Schema(description = "属性定义")
    private List<TmBaseDataTypeInfo> attributes;
}
