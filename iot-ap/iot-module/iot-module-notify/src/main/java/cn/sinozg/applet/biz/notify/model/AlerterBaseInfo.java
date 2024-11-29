package cn.sinozg.applet.biz.notify.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 基础类
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-20 13:42
 */
@Setter
@Getter
public class AlerterBaseInfo {

    @Schema(description = "是否启用")
    private boolean enable = true;

    @Schema(description = "是否应用匹配所有")
    private boolean matchAll = true;

    @Schema(description = "匹配告警信息标签(monitorId:xxx,monitorName:xxx)")
    private List<AlerterTagItemInfo> tags;

    @Schema(description = "告警级别")
    private List<String> priorities;
}
