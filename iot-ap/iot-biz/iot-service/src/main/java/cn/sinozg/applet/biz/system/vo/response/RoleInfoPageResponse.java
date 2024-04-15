package cn.sinozg.applet.biz.system.vo.response;

import cn.sinozg.applet.common.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
* 角色分页
* @Author: xyb
* @Description:
* @Date: 2023-04-11 下午 08:56
**/
@Data
public class RoleInfoPageResponse {

    /** 主键 */
    @Schema(description = "主键")
    private String id;

    /** 角色编码 */
    @Schema(description = "角色编码")
    private String roleCode;

    /** 角色名称 */
    @Schema(description = "角色名称")
    private String roleName;

    /** 角色描述 */
    @Schema(description = "角色描述")
    private String roleDesc;

    /** 创建时间 */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createdTime;

}
