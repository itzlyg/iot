package cn.sinozg.applet.iot.protocol.vo.response;

import lombok.Data;


/**
 * 协议解析脚本注册
 * @Description
 * @Copyright Copyright (c) 2023
 * @author xieyubin
 * @since 2023-11-16 12:20:34
 */
@Data
public class ProtocolAnalysisRegisterResponse {

    private String id;

    /**
     * 脚本类型
     */
    private String type;
}
