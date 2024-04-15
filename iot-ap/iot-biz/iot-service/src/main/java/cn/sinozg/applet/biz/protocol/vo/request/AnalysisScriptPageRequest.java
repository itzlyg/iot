package cn.sinozg.applet.biz.protocol.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 数据解析脚本表 分页请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
@Schema(name = "AnalysisScriptPageRequest", description = "数据解析脚本 分页请求参数")
public class AnalysisScriptPageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private String uid;
    /** 脚本名称 */
    @Schema(description = "脚本名称")
    private String scriptName;

    @Schema(description = "开始时间 yyyy-MM-dd")
    private String beginTime;

    @Schema(description = "结束时间 yyyy-MM-dd")
    private String endTime;
}
