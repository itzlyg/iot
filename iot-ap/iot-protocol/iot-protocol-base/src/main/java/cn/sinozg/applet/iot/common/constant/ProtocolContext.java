package cn.sinozg.applet.iot.common.constant;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-15 21:55
 */
public class ProtocolContext {
    public static final String TYPE_ACK = "ack";

    /** 协议的脚本 */
    public static final String SCRIPT_MODULE_FILE_NAME = "module.js";

    public static final String SCRIPT_ANALYSIS_FILE_NAME = "analysis.js";

    public static final String SPI_MODULE_FILE_NAME = "module.spi";

    public static final String SPI_ANALYSIS_NAME = "analysis.spi";

    /**** bean name ****/

    /** 设备协议管理器 */
    public static final String PROTOCOL_MANAGER = "protocolManager";

    /** 设备协议管理器 业务 */
    public static final String PROTOCOL_BIZ_MANAGER = "protocolBizManager";

    public static final String TYPE_JS = "JavaScript";
    public static final String TYPE_LUA = "LuaScript";

    /** 脚本编码方法 */
    public static final String SCRIPT_METHOD_DECODE = "decode";

    /** 脚本解码方法 */
    public static final String SCRIPT_METHOD_ENCODE = "encode";

    public static final String SCRIPT_METHOD_RECEIVE = "onReceive";
    /** java 解码方法， */
    public static final String JAVA_ANALYSIS_BEAN = "javaAnalysisBeanSystem";

    /** 类型 ***/
    public static final String TP_AUTH = "auth";

    public static final String TP_REGISTER = "register";

    public static final String TP_STATE = "state";
    public static final String TP_REPORT = "report";
    public static final String TP_OTA = "ota";

    public static final String NULL = "null";

    public static final String PING = "ping";

    public static final String PONG = "pong";

}
