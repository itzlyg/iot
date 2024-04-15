package cn.sinozg.applet.common.constant;

/**
 * redis key 常量
 * @Author: xyb
 * @Description:
 * @Date: 2023-05-11 下午 09:57
 **/
public class RedisKey extends BaseRedisKeys {

    /** 大文件存储 */
    public static final String LARGE_FILE = REDIS_PREFIX + "large_file:%s";

    /** 设备属性 */
    public static final String DEVICE_PROPERTY = REDIS_PREFIX + "device_prop:%s";
    /** 请求名称 */
    public static final String MID_NAME = REDIS_PREFIX + "mid:%s";

    /** 设备属性 表是否存在 */
    public static final String DEVICE_PROPERTY_TB = REDIS_PREFIX + "device_prop_tb:%s";
    /** OSS_TOKEN bucketName */
    public static final String OSS_TOKEN = REDIS_PREFIX + "oss_token:%s";
    /** 企业微信 token */
    public static final String WW_ACCESS_TOKEN = REDIS_PREFIX + "ww_token:%s";

    /** 缓存 规则 */
    public static final String ALERTER_RULE = REDIS_PREFIX + "al_rule";
    /** oauth2 客户端 */
    public static final String OAUTH_CLIENT = REDIS_PREFIX + "oauth_client:%s";
    /** 上行序列码 */
    public static final String SEQ_NO_UP = REDIS_PREFIX + "seq_no_up";
    /** 下行序列码 */
    public static final String  SEQ_NO_DOWN = REDIS_PREFIX + "seq_no_down";
    /** 任务码 */
    public static final String TASK_NO = REDIS_PREFIX + "task_no";
}