package cn.sinozg.applet.biz.system.vo.response;

import cn.sinozg.applet.common.core.model.TreeSelect;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户菜单 用于系统菜单数的展示
 * @Author: xyb
 * @Description:
 * @Date: 2023-04-11 下午 09:51
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSystemMenuResponse extends TreeSelect {

    @Schema(description = "小图标")
    @JsonProperty("icon")
    private String menuIcon;

    /** 路径 */
    @Schema(description = "路径")
    @JsonProperty("path")
    private String menuUrl;
}