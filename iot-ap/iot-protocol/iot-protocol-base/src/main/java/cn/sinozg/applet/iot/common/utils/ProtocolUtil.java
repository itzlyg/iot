package cn.sinozg.applet.iot.common.utils;

import cn.sinozg.applet.biz.com.model.TmMessageInfo;
import cn.sinozg.applet.common.config.SystemConfig;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.holder.UserContextHolder;
import cn.sinozg.applet.common.properties.ProtocolProperties;
import cn.sinozg.applet.common.utils.DateUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import cn.sinozg.applet.iot.common.constant.ProtocolContext;
import cn.sinozg.applet.iot.common.constant.ProtocolRedisKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 11:39
 */
@Slf4j
public class ProtocolUtil {

    private static final int MACHINE_ID = RandomUtils.nextInt(10, 99);

    private static final int MAX = 5000;

    private static final long MIN = 1000L;

    public static String requestId() {
        return uniqueId("RID");
    }

    public static String uniqueId(String prefix) {
        String key = String.format(ProtocolRedisKey.UNIQUE_ID, prefix.toLowerCase());
        long seq = RedisUtil.getIncr(key, null, () -> MIN);
        if (seq >= MAX) {
            RedisUtil.setIncr(key, MIN);
        }
        return prefix + DateUtil.formatDateTime(LocalDateTime.now(), DateUtil.YYYYMMDDHHMMSS) + seq + MACHINE_ID;
    }


    /**
     * 将脚本写到本地磁盘
     * @param id id
     * @param analysis 是否为数据解析脚本
     * @param script 脚本
     */
    public static void writeScript(String id, boolean analysis, String script){
        File file = scriptFile(id, analysis ? BaseConstants.STATUS_01 : BaseConstants.STATUS_00);
        try {
            if (StringUtils.isBlank(script)) {
                FileUtils.delete(file);
            } else {
                FileUtils.writeStringToFile(file, script, Charset.defaultCharset());
            }
        } catch (IOException e) {
            log.error("创建本地脚本失败！", e);
            throw new CavException("在本地保存脚本失败！");
        }
    }

    /**
     * 读取本地磁盘的脚本
     * @param id id
     * @param analysis 是否为数据解析脚本
     */
    public static String readScript(String id, boolean analysis){
        File file = scriptFile(id, analysis ? BaseConstants.STATUS_01 : BaseConstants.STATUS_00);
        if (!file.exists()) {
            return null;
        }
        try {
            return FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (IOException e) {
            log.error("读取本地脚本失败！", e);
            throw new CavException("读取本地脚本失败！");
        }
    }

    /**
     * jar包位置
     * @param id jarId
     * @param fileName 文件名称
     * @return jar包
     */
    public static File jarFile (String id, String fileName){
        return file(id, BaseConstants.AT, fileName);
    }

    /**
     * 根据id获取到jar包
     * @param id id
     * @return jar包
     */
    public static File jarFile (String id){
        Path path = path(id, BaseConstants.AT);
        File jarRoot = path.toFile();
        if (jarRoot.isDirectory()) {
            File[] subFile = jarRoot.listFiles();
            if (ArrayUtils.isNotEmpty(subFile)) {
                for (File file : subFile) {
                    if (StringUtils.endsWith(file.getName(), ".jar")) {
                        return file;
                    }
                }
            }
        }
        throw new CavException("未找到对应的jar包文件！");
    }

    /**
     * 删除文件
     * @param file 文件
     */
    public static boolean deleteFile (File file){
        if (file != null && file.exists()) {
            boolean result = file.delete();
            log.info("删除文件{} ，{}", file.getName(), result);
            return result;
        }
        return false;
    }

    /**
     * 读取文件
     * @param id id
     * @param type 00:协议脚本 01:转化器
     * @return 文件
     */
    public static File scriptFile (String id, String type){
        String fileName = ProtocolContext.SCRIPT_MODULE_FILE_NAME;
        if (BaseConstants.STATUS_01.equals(type)) {
            fileName = ProtocolContext.SCRIPT_ANALYSIS_FILE_NAME;
        }
        return file(id, type, fileName);
    }


    /**
     * 设置 租户
     * @param message 消息
     */
    public static void setTenantId(TmMessageInfo message){
        if (StringUtils.isBlank(message.getTenantId())) {
            message.setTenantId(UserContextHolder.getTenantId());
        } else {
            UserContextHolder.setTenantId(message.getTenantId());
        }
    }

    /**
     * 文件信息
     * @param id id
     * @param type 00:协议脚本 01:转化器 其他:jar
     * @param fileName 文件名称
     * @return 文件
     */
    private static File file (String id, String type, String fileName){
        Path path = path(id, type);
        return path.resolve(fileName).toFile();
    }

    /**
     * 文件信息
     * @param id id
     * @param type 00:协议脚本 01:转化器 其他:jar
     * @return 文件
     */
    private static Path path(String id, String type){
        ProtocolProperties properties = SystemConfig.APP.getProtocol();
        String first = properties.getJarDir();
        if (BaseConstants.STATUS_00.equals(type)) {
            first = properties.getModuleDir();
        } else if (BaseConstants.STATUS_01.equals(type)) {
            first = properties.getAnalysisDir();
        }
        return Paths.get(first, id).toAbsolutePath().normalize();
    }
}
