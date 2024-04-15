package cn.sinozg.applet.biz.protocol.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* 数据解析脚本表 修改请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
public class AnalysisScriptUpdateTextRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    @NotBlank(message = "id不能为空")
    private String id;

    /** 脚本 */
    @Schema(description = "脚本")
    @NotBlank(message = "脚本为空")
    private String script;

}
