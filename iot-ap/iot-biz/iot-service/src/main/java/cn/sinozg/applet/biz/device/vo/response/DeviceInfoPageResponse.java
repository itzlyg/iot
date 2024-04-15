package cn.sinozg.applet.biz.device.vo.response;

import cn.sinozg.applet.common.annotation.DictTrans;
import cn.sinozg.applet.common.enums.DictType;
import cn.sinozg.applet.common.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 设备信息表表 分页返回参数
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@Schema(name = "DeviceInfoPageResponse", description = "设备信息表 分页返回参数")
public class DeviceInfoPageResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 设备编码 */
    @Schema(description = "设备编码")
    private String deviceCode;

    /** 设备名称 */
    @Schema(description = "设备名称")
    private String deviceName;

    /** 产品名称 */
    @Schema(description = "产品名称")
    private String prodName;

    /** 设备类型 */
    @Schema(description = "设备类型")
    @DictTrans(type = DictType.DEVICE_TYPE)
    private String deviceType;

    /** 顺序号 */
    @Schema(description = "顺序号")
    private Integer seqNo;

    /** 是否有效0无效1有效 */
    @Schema(description = "是否有效0无效1有效")
    private Integer isValid;

    /** 创建时间 */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createdTime;
}
