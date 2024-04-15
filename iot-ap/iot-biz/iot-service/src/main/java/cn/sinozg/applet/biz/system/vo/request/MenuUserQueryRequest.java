package cn.sinozg.applet.biz.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
* 获取用户的菜单信息
* @Author: xyb
* @Description: 
* @Date: 2023-04-11 下午 09:47
**/
@Data
public class MenuUserQueryRequest {

    @NotBlank(message = "渠道不能为空")
    @Schema(description = "渠道 01web 02 app")
    private String channel;
}
