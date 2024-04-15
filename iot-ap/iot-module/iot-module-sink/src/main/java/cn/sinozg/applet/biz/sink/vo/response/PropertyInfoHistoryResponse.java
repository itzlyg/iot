package cn.sinozg.applet.biz.sink.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-05 20:25
 */
@Data
public class PropertyInfoHistoryResponse {

    private String deviceCode;

    private Object value;

    @JsonIgnore
    private Long ts;

    private LocalDateTime dateTime;
}
