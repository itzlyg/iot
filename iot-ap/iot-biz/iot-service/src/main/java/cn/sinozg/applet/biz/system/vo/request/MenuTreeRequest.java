package cn.sinozg.applet.biz.system.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
* 菜单树查询
* @Author: xyb
* @Description:
* @Date: 2023-04-11 下午 10:37
**/
@Data
public class MenuTreeRequest {

    @Schema(description = "菜单编号")
    private String menuCode;

    @NotBlank(message = "渠道不能为空")
    @Schema(description = "渠道")
    private String channel;

    @JsonIgnore
    private String userId;
}
