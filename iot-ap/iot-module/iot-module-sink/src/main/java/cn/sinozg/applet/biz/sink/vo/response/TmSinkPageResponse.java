package cn.sinozg.applet.biz.sink.vo.response;

import cn.sinozg.applet.common.annotation.DictTrans;
import cn.sinozg.applet.common.enums.DictType;
import cn.sinozg.applet.common.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分页返回数据
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 17:01:45
 */
@Data
public class TmSinkPageResponse {

    @Schema(description = "请求id")
    private String mid;

    @Schema(description = "请求名称")
    private String midName;

    @Schema(description = "产品key")
    private String prodKey;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "所属用户")
    private String uid;

    @Schema(description = "类型")
    @DictTrans(type = DictType.TM_TYPE)
    private String type;

    @Schema(description = "标志符号")
    private String identifier;

    @Schema(description = "消息状态码")
    private int code;

    @Schema(description = "具体消息，Map结构")
    private Object data;

    @Schema(description = "时间戳，设备上的事件或数据产生的本地时间")
    private LocalDateTime reportTime;

    @JsonIgnore
    private Long ts;

    @Schema(description = "时间")
    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return DateUtil.ldt(ts);
    }
}
