package cn.sinozg.applet.biz.product.vo.response;

import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-01-12 11:37
 */
@Getter
@Setter
public class ProdListResponse extends DictListResponse {

    @Schema(description = "id")
    private String id;
}
