package cn.sinozg.applet.biz.system.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门详情 列表
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-09-13 11:19
 */
@Data
public class DeptInfoPageResponse {
    /** 主键id */
    @Schema(description = "主键id")
    private String id;

    /** 部门名称 */
    @Schema(description = "部门名称")
    private String deptName;

    /** 负责人姓名 */
    @Schema(description = "负责人姓名")
    private String directorName;

    @Schema(description = "是否有下级")
    private boolean hasChildren;

    @Schema(description = "子部门")
    private List<DeptInfoPageResponse> children;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
}
