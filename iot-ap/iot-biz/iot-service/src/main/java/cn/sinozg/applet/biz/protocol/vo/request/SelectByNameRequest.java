package cn.sinozg.applet.biz.protocol.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-21 17:10
 */
@Data
public class SelectByNameRequest {
    @JsonIgnore
    private String uid;
    @Schema(description = "名称，模糊查询")
    private String name;
}
