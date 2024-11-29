package cn.sinozg.applet.biz.notify.model.collect;

import lombok.Data;

/**
 * Monitoring configuration parameter properties and values
 * During the process, you need to replace the content with the identifier ^_^key^_^
 * in the protocol configuration parameter with the real value in the configuration parameter
 * @Description 
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 12:53:12
 */
@Data
public class Configmap {

    /**
     * Parameter key, replace the content with the identifier ^^_key_^^ in the protocol
     * configuration parameter with the real value in the configuration parameter
     * <p>
     * 参数key,将协议配置参数里面的标识符为^^_key_^^的内容替换为配置参数里的真实值
     */
    private String key;

    /**
     * parameter value  参数value
     */
    private Object value;

    /**
     * Parameter type 0: number 1: string 2: encrypted string 3: json string mapped by map
     * 参数类型 0:数字 1:字符串 2:加密串 3:map映射的json串 4:arrays string
     * number,string,secret
     * 数字,非加密字符串,加密字符串
     */
    private byte type = 1;
}
