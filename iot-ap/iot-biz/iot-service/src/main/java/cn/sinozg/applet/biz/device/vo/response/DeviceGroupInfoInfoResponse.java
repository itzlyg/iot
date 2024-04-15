package cn.sinozg.applet.biz.device.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* 设备分组信息表表 详情返回信息
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@Schema(name = "DeviceGroupInfoInfoResponse", description = "设备分组信息表 详情返回信息")
public class DeviceGroupInfoInfoResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 分组名称 */
    @Schema(description = "分组名称")
    private String groupName;

    /** 分组归属用户 */
    @Schema(description = "分组归属用户")
    private String groupOwner;

    /** 分组说明 */
    @Schema(description = "分组说明")
    private String groupDesc;

    /** 设备JSON数据：存储与分组相关的设备信息 */
    @Schema(description = "设备JSON数据：存储与分组相关的设备信息")
    private List<String> groupDeviceJson;

    /** 顺序号 */
    @Schema(description = "顺序号")
    private Integer seqNo;

    /** 是否有效0无效1有效 */
    @Schema(description = "是否有效0无效1有效")
    private Integer isValid;

}
