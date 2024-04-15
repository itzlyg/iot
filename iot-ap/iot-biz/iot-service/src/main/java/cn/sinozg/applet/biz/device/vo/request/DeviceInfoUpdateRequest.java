package cn.sinozg.applet.biz.device.vo.request;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
* 设备信息表表 修改请求参数
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@Schema(name = "DeviceInfoUpdateRequest", description = "设备信息表 修改请求参数")
public class DeviceInfoUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    @NotBlank(message = "id不能为空")
    private String id;

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
