package cn.sinozg.applet.common.enums;

import lombok.Getter;

/**
* 数据字段
* @Author: xyb
* @Description:
* @Date: 2022-11-14 下午 09:38
**/
@Getter
public enum DictType {
    /** 根 **/
    ROOT_TYPE("JB_ROOT"),
    /** 性别 **/
    GENDER("GENDER"),
    /** 数据状态 **/
    DATA_STATUS("DATA_STATUS"),
    /** 租户 */
    TENANT ("TENANT"),
    /** 协议类型 */
    PROTOCOL_TYPE("PROTOCOL_TYPE"),
    /** 脚本类型 */
    SCRIPT_TYPE("SCRIPT_TYPE"),
    /** tcp 分割方式 */
    PARSER_TYPE("PARSER_TYPE"),
    /** 数据类型 */
    DATA_TYPE("DATA_TYPE"),
    /** 设备类型 */
    DEVICE_TYPE("DEVICE_TYPE"),
    /** 物模型类型 */
    TM_TYPE("TM_TYPE"),
    /** 物模型标志符号 */
    TM_IDENTIFIER("TM_IDENTIFIER"),

    /** 设备任务状态 */
    TASK_MANGE_TYPE("TASK_MANGE_TYPE"),
    /** 触发场景 */
    TRIGGER_TYPE("TRIGGER_TYPE"),
    /** 告警级别 */
    ALERT_PRIORITY("ALERT_PRIORITY"),
    /** 告警通知方式 */
    ALERTER_CHANNEL_TYPE("ALERTER_CHANNEL_TYPE"),
    ;

    private final String code;
    DictType(String code) {
        this.code = code;
    }
    public static DictType byCode (String code){
        for (DictType bt : DictType.values()) {
            if (bt.getCode().equals(code)) {
                return bt;
            }
        }
        return ROOT_TYPE;
    }
}
