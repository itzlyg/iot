package cn.sinozg.applet.instruct.util;

import cn.sinozg.applet.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 编码解码相关的工具类
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2024
 * @since 2024-03-23 11:16
 */
@Slf4j
public class CodecUtil {

    private static final int TWO = 2;

    private static final String[] HM = {"HH", "mm"};

    private static final char[] HEX = "0123456789abcdef".toCharArray();

    /**
     * 上报过来的时间转为 时间
     * @param input 时间字符串
     * @param pattern 时间字格式
     * @return 时间
     */
    public static LocalDateTime ldtTime (String input, String pattern){
        if (StringUtils.isAnyBlank(input, pattern)) {
            return null;
        }
        // 只有时分秒
        if (StringUtils.startsWithAny(pattern, HM)) {
            LocalTime lt = DateUtil.parseTime(input, pattern);
            LocalDate ld = LocalDate.now();
            if (lt.isAfter(LocalTime.now())) {
                ld = ld.minusDays(1);
            }
            return ld.atTime(lt);
        } else {
            return DateUtil.parseDateTime(input, pattern);
        }
    }

    /**
     * 时间转字符串
     * @param input 时间
     * @param pattern 格式
     * @return 字符串
     */
    public static String ldtTime (LocalDateTime input, String pattern){
        if (input == null || StringUtils.isAnyBlank(pattern)) {
            return null;
        }
        // 只有时分秒
        if (StringUtils.startsWithAny(pattern, HM)) {
            return DateUtil.formatTime(input.toLocalTime(), pattern);
        } else {
            return DateUtil.formatDateTime(input, pattern);
        }
    }

    /**
     * 十六进制转十进制(无符号)
     *
     * @param hex 十六进制
     * @return 十进制
     */
    public static int hexToInt(String hex) {
        int result = 0;
        if (StringUtils.isNotBlank(hex)) {
            try {
                BigInteger bi = new BigInteger(hex, 16);
                result = bi.intValue();
            } catch (Exception e) {
                log.error("十六进制转十进制错误：" + hex, e);
            }
        }
        return result;
    }


    /**
     * 转换为2Byte 16进制字符
     *
     * @param input 数字
     * @return 结果
     */
    private static String intToHex(int input) {
        return Integer.toHexString(input);
    }

    /**
     * 数字转 16进制字符
     * @param input 数字
     * @param bs 长度
     * @return 字符串
     */
    public static String intToHex(int input, int bs) {
        return leftPad(intToHex(input), bs * TWO);
    }

    /**
     * 十进制转二进制
     * @param input 值
     * @param len 长度
     * @return 值
     */
    public static String intToBin(int input, int len){
        String bin = Integer.toBinaryString(input);
        return CodecUtil.leftPad(bin, len);
    }

    /**
     * 十六进制转浮点
     * @param input 十六进制
     * @return 浮点
     */
    public static float hexToFloat(String input){
        byte[] byteArray = new byte[4];
        for (int i = 0, j = 0; i < input.length(); i += 2, j++) {
            byteArray[j] = (byte) Integer.parseInt(input.substring(i, i + 2), 16);
        }
        return ByteBuffer.wrap(byteArray).getFloat();
    }

    /**
     * 16进制ASCII->String
     *a
     * @param ascii ascii
     * @return 字符串
     */
    public static String asciiToString(String ascii) {
        StringBuilder sbu = new StringBuilder();
        for (int i = 0; i < ascii.length(); i += TWO) {
            sbu.append((char) hexToInt(ascii.substring(i, i + TWO)));
        }
        return sbu.toString();
    }

    /**
     * String->16进制ASCII
     *
     * @param message 消息
     * @return ascii 码
     */
    public static String stringToAscii(String message) {
        StringBuilder sbu = new StringBuilder();
        char[] chars = message.toCharArray();
        for (char c : chars) {
            sbu.append(intToHex(c));
        }
        return sbu.toString();
    }

    /**
     * 16进制转二进制
     * @param input 十六进制
     * @param bs 长度
     * @return 二进制
     */
    public static String hexToBinary(String input, int bs){
        int t = hexToInt(input);
        return leftPad(Integer.toBinaryString(t), bs * 8);
    }

    /**
     * 二进制转为十六进制
     * @param binary 二进制
     * @return 十六进制
     */
    public static String binaryToHex(String binary){
        int t = Integer.parseInt(binary, TWO);
        return Integer.toHexString(t);
    }


    /**
     * 转换为2Byte 16进制字符
     *
     * @param input 数字
     * @return 结果
     */
    public static String byteToHex(byte input) {
        return byteToHex(input, TWO);
    }

    /**
     * 转换为 16进制字符
     *
     * @param input 数字
     * @return 结果
     */
    public static String byteToHex(byte input, int bs) {
        String format = "%0" + bs + "X";
        return String.format(format, input);
    }
    /**
     * 字符左补零
     *
     * @param input 输入
     * @param length 长度
     * @return 字符串
     */
    public static String leftPad(String input, int length) {
        return StringUtils.leftPad(input, length, "0");
    }

    /**
     * 字节码 转字符串
     * @param bytes 字节
     * @return 字符串
     */
    public static String toHexString(byte[] bytes) {
        if (null == bytes) {
            return null;
        }
        StringBuilder sb = new StringBuilder(bytes.length << 1);
        for (byte aByte : bytes) {
            sb.append(HEX[(aByte & 0xf0) >> 4]).append(HEX[(aByte & 0x0f)]);
        }
        return sb.toString();
    }



    /**
     * 二进制转10进制
     * @param input 二进制
     * @return 十进制
     */
    public static Integer binaryToInt(String input){
        return Integer.parseInt(input, TWO);
    }


    /**
     * 字符串2位一截取 反转
     * @param input 数据
     * @return 反转 数据
     */
    public static String revertStrLen2(String input){
        if (StringUtils.isBlank(input) || input.length() % TWO != 0) {
            return input;
        }
        List<String> list = new ArrayList<>();
        for (int i = input.length() - TWO; i >= 0; i = i - TWO) {
            list.add(input.substring(i, i + TWO));
        }
        return StringUtils.join(list, StringUtils.EMPTY);
    }
}
