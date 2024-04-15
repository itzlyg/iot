package cn.sinozg.applet.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-12 17:03
 */
public class BizUtil {
    private static final Pattern PATTERN = Pattern.compile("([^&=]+)=([^&=]+)");

    /**
     * 将请求参数转为 实体对象
     * @param query 请求参数
     * @param clazz 实体
     * @return 实体
     * @param <T> 实体类型
     */
    public static <T> T paramsBean (String query, Class<T> clazz){
        Map<String, Object> map = new HashMap<>(64);
        Matcher m = PATTERN.matcher(query);
        while (m.find()) {
            map.put(m.group(1), m.group(2));
        }
        return JsonUtil.mapPojo(map, clazz);
    }

    /**
     * 获取一个由字母和数据组成的随机数code，且判断是否已经存在
     * @param len 长度
     * @param service 数据库查询 对应的service
     * @param fieldFun 对应的字段
     * @return 新的编号
     */

    public static <T> String randomCode(int len, IService<T> service, SFunction<T, String> fieldFun){
        int batch = 10;
        while (true) {
            List<String> codes = new ArrayList<>();
            for (int i = 0; i < batch; i++) {
                codes.add(RandomStringUtils.randomAlphanumeric(len));
            }
            List<T> list = service.list(new LambdaQueryWrapper<T>().in(fieldFun, codes));
            if (!(CollectionUtils.isNotEmpty(list) && list.size() == batch)) {
                List<String> dbCodes = PojoUtil.toList(list, fieldFun);
                for (String code : codes) {
                    if (!dbCodes.contains(code)) {
                        return code;
                    }
                }
            }
        }
    }

    /**
     * 是否为演示用户
     * @return 是否为演示用户
     */
    public static String demoUser (){
        String uid = UserUtil.uid();
        String dbUid = "291185923923353600";
        if (!dbUid.equals(uid)) {
            return null;
        }
        return uid;
    }
}
