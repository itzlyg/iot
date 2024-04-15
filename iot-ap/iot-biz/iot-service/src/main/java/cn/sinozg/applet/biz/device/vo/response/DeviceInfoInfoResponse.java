package cn.sinozg.applet.biz.device.vo.response;

import cn.sinozg.applet.biz.device.vo.response.device.AttributeElementInfo;
import cn.sinozg.applet.biz.device.vo.response.device.FunctionElement;
import cn.sinozg.applet.common.annotation.DictTrans;
import cn.sinozg.applet.common.enums.DictType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* 设备信息表表 详情返回信息
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@Schema(name = "DeviceInfoInfoResponse", description = "设备信息表 详情返回信息")
public class DeviceInfoInfoResponse implements Serializable {

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

    /** 产品key */
    @Schema(description = "产品key")
    private String prodKey;

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

    @Schema(description = "属性信息表格元素")
    private List<AttributeElementInfo> attributeInfoList;

    @Schema(description = "功能描述定义")
    private List<FunctionElement> functionInfoList;

}
