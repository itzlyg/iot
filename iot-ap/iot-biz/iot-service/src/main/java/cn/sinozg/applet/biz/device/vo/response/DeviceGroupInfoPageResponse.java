package cn.sinozg.applet.biz.device.vo.response;

import cn.sinozg.applet.common.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 设备分组信息表表 分页返回参数
* @Description
* @Copyright Copyright (c) 2023
* @author wang.pf
* @since 2023-12-03 16:27:01
*/
@Data
@Schema(name = "DeviceGroupInfoPageResponse", description = "设备分组信息表 分页返回参数")
public class DeviceGroupInfoPageResponse implements Serializable {

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

    @Schema(description = "分组归属用户 名称")
    private String ownerName;

    /** 分组说明 */
    @Schema(description = "分组说明")
    private String groupDesc;

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
