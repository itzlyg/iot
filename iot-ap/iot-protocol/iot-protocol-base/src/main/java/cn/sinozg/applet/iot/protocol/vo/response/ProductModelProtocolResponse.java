package cn.sinozg.applet.iot.protocol.vo.response;

import lombok.Data;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 11:15
 */
@Data
public class ProductModelProtocolResponse {

    private String id;

    /**
     * 型号在所有产品中唯一
     */
    private String model;

    private String name;

    private String prodKey;

    private String type;

    private String script;

    /**
     * 脚本状态，只有发布状态才生效
     */
    private String state;

    private Long modifyAt;
}
