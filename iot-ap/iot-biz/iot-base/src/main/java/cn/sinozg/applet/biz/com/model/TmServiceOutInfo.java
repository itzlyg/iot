package cn.sinozg.applet.biz.com.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 物模型 服务数据信息
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-07 22:19
 */
@Data
public class TmServiceOutInfo {

    @Schema(description = "属性标识 即key")
    private String identifier;

    @Schema(description = "输入")
    private List<TmBaseDataTypeInfo> inputData;
}
