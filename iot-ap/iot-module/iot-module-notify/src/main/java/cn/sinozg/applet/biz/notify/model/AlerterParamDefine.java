package cn.sinozg.applet.biz.notify.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description 
 * @Copyright Copyright (c) 2024
 * @author xieyubin
 * @since 2024-01-22 18:59:00
 */
@Data
public class AlerterParamDefine {

    @Schema(description = "参数结构ID")
    private String id;

    @Schema(description = "监控应用类型名称")
    private String app;

    /**
     * Parameter field external display name
     * zh-CN: 端口
     * en-US: Port
     * 参数字段对外显示名称
     */
    @Schema(description = "参数字段对外显示名称 ")
    private Map<String, String> name;

    /**
     * Parameter Field Identifier
     * 参数字段标识符
     */
    @Schema(description = "参数字段标识符")
    private String field;

    /**
     * Field type, style (mostly map the input tag type attribute)
     * 字段类型,样式(大部分映射input标签type属性)
     */
    @Schema(description = "字段类型,样式(大部分映射input标签type属性)")
    private String type;

    /**
     * Is it mandatory true-required false-optional
     * 是否是必输项 true-必填 false-可选
     */
    @Schema(description = "是否是必输项 true-必填 false-可选")
    private boolean required = false;

    @Schema(description = "参数默认值")
    private String defaultValue;

    @Schema(description = "参数输入框提示信息")
    private String placeholder;

    /**
     * When type is number, use range to represent the range eg: 0-233
     * 当type为number时,用range表示范围 eg: 0-233
     */
    @Schema(description = "当type为number时,用range区间表示范围")
    private String range;

    /**
     * When type is text, use limit to indicate the limit size of the string. The maximum is 255
     * 当type为text时,用limit表示字符串限制大小.最大255
     */
    @Schema(description = "当type为text时,用limit表示字符串限制大小.最大255")
    private Short limit;

    /**
     * When the type is radio radio box, checkbox checkbox, options represents a list of optional values
     * 当type为radio单选框,checkbox复选框时,options表示可选项值列表
     * eg: {
     * "key1":"value1",
     * "key2":"value2"
     * }
     * key-值显示标签
     * value-真正值
     */
    @Schema(description = "当type为radio单选框,checkbox复选框时,option表示可选项值列表")
    private List<AlerterParamDefineOption> options;

    /**
     * Valid when type is key-value, indicating the alias description of the key
     * 当type为key-value时有效,表示key的别名描述
     */
    @Schema(description = "当type为key-value时有效,表示key的别名描述")
    private String keyAlias;

    /**
     * Valid when type is key-value, indicating the alias description of value type
     * 当type为key-value时有效,表示value的别名描述
     */
    @Schema(description = "当type为key-value时有效,表示value的别名描述")
    private String valueAlias;

    /**
     * Is it an advanced hidden parameter true-yes false-no
     * 是否是高级隐藏参数 true-是 false-否
     */
    @Schema(description = "是否是高级隐藏参数 true-是 false-否")
    private boolean hide = false;
}
