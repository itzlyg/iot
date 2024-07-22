package cn.sinozg.applet.biz.com.model;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.biz.com.enums.TmType;
import cn.sinozg.applet.common.annotation.EnumField;
import cn.sinozg.applet.common.utils.JsonUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.tika.utils.StringUtils;

/**
 * 物模型 信息 用来接收消息
 * 协议使用
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 17:01:45
 */
@Data
public class TmMessageInfo {

    @Schema(description = "id 主键，可以不返回")
    private String id;

    @Schema(description = "请求id")
    private String mid;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "产品key")
    private String prodKey;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "所属用户")
    private String uid;

    /**
     * 消息类型
     * @see TmType
     */
    @Schema(description = "消息类型")
    @EnumField(clazz = TmType.class, key = "enName")
    private String type;

    /**
     * 事件标准符
     * @see TmIdentifierType
     */
    @EnumField(clazz = TmIdentifierType.class, key = "enName")
    @Schema(description = "事件标准符，加上\"_reply\"表示 回复")
    private String identifier;

    @Schema(description = "指令类型")
    private String orderTp;

    @Schema(description = "消息状态码")
    private int code;

    @Schema(description = "租户")
    private String tenantId;

    @Schema(description = "具体消息对象")
    private Object data;

    @Schema(description = "要保存的数据")
    private String saveParams;

    @Schema(description = "时间戳，设备上的事件或数据产生的本地时间")
    private Long occurred;

    @Schema(description = "消息上报时间")
    private Long time;

    public void setType(TmType type) {
        this.type = type.getEnName();
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setIdentifier(TmIdentifierType identifier) {
        this.identifier = identifier.getEnName();
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSaveParams() {
        if (StringUtils.isBlank(saveParams) && data != null) {
            return JsonUtil.toJson(data);
        }
        return saveParams;
    }
}
