package cn.sinozg.applet.iot.protocol.model;

import cn.sinozg.applet.biz.com.enums.TmIdentifierType;
import cn.sinozg.applet.common.annotation.EnumField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * 设备状态信息
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-15 22:06:19
 */
@Getter
@Setter
@FieldNameConstants
@Schema(description = "设备认证 type 为 auth")
public class DeviceStateInfo extends BaseDeviceInfo {

    /**
     * 映射到
     * @see TmIdentifierType
     * 的 enName
     */
    @Schema(description = "状态")
    @EnumField(clazz = TmIdentifierType.class, key = "enName")
    private String state;

    @Schema(description = "父设备")
    private DeviceBaseInfo parent;
}
