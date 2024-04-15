package cn.sinozg.applet.biz.system.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 部门信息表 分页查询
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-09-06 11:22:14
*/
@Data
public class DeptInfoPageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 父id */
    @JsonIgnore
    private String paterId;

    /** 部门名称 */
    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "开始时间 yyyy-MM-dd")
    private String beginTime;

    @Schema(description = "结束时间 yyyy-MM-dd")
    private String endTime;
}
