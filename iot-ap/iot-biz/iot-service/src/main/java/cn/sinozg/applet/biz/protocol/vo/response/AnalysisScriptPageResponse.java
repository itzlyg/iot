package cn.sinozg.applet.biz.protocol.vo.response;

import cn.sinozg.applet.common.annotation.DictTrans;
import cn.sinozg.applet.common.enums.DictType;
import cn.sinozg.applet.common.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 数据解析脚本表 分页返回参数
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Data
@Schema(name = "AnalysisScriptPageResponse", description = "数据解析脚本 分页返回参数")
public class AnalysisScriptPageResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 脚本名称 */
    @Schema(description = "脚本名称")
    private String scriptName;

    /** 脚本说明 */
    @Schema(description = "脚本说明")
    private String scriptDesc;

    /** 数据状态;00 未启用 01 启用 */
    @Schema(description = "数据状态;00 未启用 01 启用")
    @DictTrans(type = DictType.DATA_STATUS)
    private String dataStatus;

    /** 创建时间 */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createdTime;
}
