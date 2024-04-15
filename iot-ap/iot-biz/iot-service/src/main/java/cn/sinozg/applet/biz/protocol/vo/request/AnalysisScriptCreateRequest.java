package cn.sinozg.applet.biz.protocol.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 数据解析脚本表 新增请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
@Schema(name = "AnalysisScriptCreateRequest", description = "数据解析脚本 新增请求参数")
public class AnalysisScriptCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 脚本名称 */
    @Schema(description = "脚本名称")
    private String scriptName;

    /** 脚本说明 */
    @Schema(description = "脚本说明")
    private String scriptDesc;
}
