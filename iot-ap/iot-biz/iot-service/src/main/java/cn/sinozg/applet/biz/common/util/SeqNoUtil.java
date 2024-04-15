package cn.sinozg.applet.biz.common.util;

import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * 顺序号工具类
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-04-10 13:06
 */
public class SeqNoUtil {

    /**
     * 生成唯一序列号 任务
     * 0x00000000<--->0x7FFFFFFF
     * 0 到 2<sup>31</sup>-1
     * @return 任务序列号
     */
    public static String taskNo (){
        Supplier<Long> supplier = () -> 0L;
        long no = RedisUtil.getIncr(RedisKey.TASK_NO, null, supplier);
        if (no > Integer.MAX_VALUE) {
            // 重置
            RedisUtil.setIncr(RedisKey.TASK_NO, 0);
            no = RedisUtil.getIncr(RedisKey.TASK_NO, null, supplier);
        }
        return hexNo(no);
    }

    /**
     * 生成唯一序列号 上行 mqtt发送到平台
     * 0x00000000<--->0x7FFFFFFF
     * 0 到 2<sup>31</sup>-1
     * @return 序列号
     */
    public static String upNo (){
        Supplier<Long> supplier = () -> 0L;
        long no = RedisUtil.getIncr(RedisKey.SEQ_NO_UP, null, supplier);
        if (no > Integer.MAX_VALUE) {
            // 重置
            RedisUtil.setIncr(RedisKey.SEQ_NO_UP, 0);
            no = RedisUtil.getIncr(RedisKey.SEQ_NO_UP, null, supplier);
        }
        return hexNo(no);
    }

    /**
     * 生成唯一序列号 下行 平台发送到mqtt
     * 0x80000000<--->0xFFFFFFFF
     * -2<sup>31</sup> 到 0
     * @return 序列号
     */
    public static String downNo (){
        Supplier<Long> supplier = () -> (long) Integer.MIN_VALUE;
        long no = RedisUtil.getIncr(RedisKey.SEQ_NO_DOWN, null, supplier);
        if (no > 0) {
            // 重置
            RedisUtil.setIncr(RedisKey.SEQ_NO_DOWN, Integer.MIN_VALUE);
            no = RedisUtil.getIncr(RedisKey.SEQ_NO_DOWN, null, supplier);
        }
        return hexNo(no);
    }

    /**
     * 16进制序列号
     * @param no 序列号
     * @return 16进制序列号
     */
    private static String hexNo (long no) {
        String hex = Integer.toHexString((int)no).toUpperCase();
        return StringUtils.leftPad(hex, 8, "0");
    }
}
