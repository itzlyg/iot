package cn.sinozg.applet.biz.device.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 设备信息表表 新增请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@Schema(name = "DeviceInfoCreateRequest", description = "设备信息表 新增请求参数")
public class DeviceInfoCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 设备编码 */
    @Schema(description = "设备编码")
    private String deviceCode;

    /** 设备名称 */
    @Schema(description = "设备名称")
    private String deviceName;

    /** 产品key */
    @Schema(description = "产品key")
    private String prodKey;

    /** 设备类型 */
    @Schema(description = "设备类型")
    private String deviceType;

    /** 顺序号 */
    @Schema(description = "顺序号")
    private Integer seqNo;

    /** 是否有效0无效1有效 */
    @Schema(description = "是否有效0无效1有效")
    private Integer isValid;

}
