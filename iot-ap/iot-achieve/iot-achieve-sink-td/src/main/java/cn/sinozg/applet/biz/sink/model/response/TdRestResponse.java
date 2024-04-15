package cn.sinozg.applet.biz.sink.model.response;

import lombok.Data;

import java.util.List;

/**
 * rest 请求返回封装
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-12-06 15:17:10
 */
@Data
public class TdRestResponse {

    private String status;

    private int code;

    private String desc;

    private List<List<Object>> data;
}
