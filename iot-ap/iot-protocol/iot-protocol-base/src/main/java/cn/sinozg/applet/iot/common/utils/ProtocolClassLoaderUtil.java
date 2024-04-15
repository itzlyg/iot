package cn.sinozg.applet.iot.common.utils;

import cn.sinozg.applet.iot.common.constant.ProtocolContext;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.PojoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过spi方式获取到实现类
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-11-16 12:23:10
 */
@Slf4j
public class ProtocolClassLoaderUtil {
    private static final Map<String, URLClassLoader> CLASS_LOADERS = new HashMap<>();

    private static String addUrl(String name, File jarPath) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        closeClassLoader(name);
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{jarPath.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
        CLASS_LOADERS.put(name, classLoader);

        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        //if (!method.canAccess(classLoader)) {
        method.setAccessible(true);
        //}
        URL url = jarPath.toURI().toURL();
        method.invoke(classLoader, url);
        return firstSpiByConfig(classLoader, ProtocolContext.SPI_MODULE_FILE_NAME, null);
    }

    public static <T> T getComponent(String name, File jarFile) throws Exception {
        String className = addUrl(name, jarFile);
        if (StringUtils.isBlank(className)) {
            throw new CavException("通过spi配置，未找到组件信息！");
        }
        return newPojo(name, className);
    }

    public static <T> T getConverter(String name) throws Exception {
        URLClassLoader classLoader = CLASS_LOADERS.get(name);
        String className = firstSpiByConfig(classLoader, ProtocolContext.SPI_ANALYSIS_NAME, "通过spi配置，未找到转化器信息！");
        return newPojo(name, className);
    }

    /**
     * 卸载类
     * @param name 名称
     */
    public static void closeClassLoader(String name) {
        try {
            URLClassLoader classLoader = CLASS_LOADERS.get(name);
            if (classLoader != null) {
                classLoader.close();
            }
        } catch (Exception e) {
            log.error("加载类错误，", e);
        }
    }

    /**
     * 创建对象实例
     * @param name 名称
     * @param className 类名称
     * @return 实例
     * @param <T> 实例类型
     * @throws Exception 异常
     */
    private static <T> T newPojo (String name, String className) throws Exception {
        ClassLoader classLoader = CLASS_LOADERS.get(name);
        Class<?> clazz = classLoader.loadClass(className);
        Class<T> cc = PojoUtil.cast(clazz);
        return PojoUtil.newInstance(cc);
    }


    /**
     * 获取spi信息的第一行
     * @param classLoader 类加载器
     * @param mame 名称
     * @param errorMsg 错误信息
     * @return 配置
     */
    private static String firstSpiByConfig(URLClassLoader classLoader, String mame, String errorMsg) {
        try (InputStream is = classLoader.getResourceAsStream(mame)) {
            if (is == null) {
                return null;
            }
            String[] lines = IOUtils.toString(is, StandardCharsets.UTF_8).split("\\s");
            if (ArrayUtils.isEmpty(lines)) {
                if (StringUtils.isEmpty(errorMsg)) {
                    return null;
                } else {
                    throw new CavException(errorMsg);
                }
            }
            return lines[0].trim();
        } catch (Exception e) {
            log.error("获取配置信息错误，", e);
            if (e instanceof CavException) {
                throw (CavException)e;
            } else {
                throw new CavException("获取spi信息错误！", e);
            }
        }
    }
}
