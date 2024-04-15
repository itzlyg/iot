package cn.sinozg.applet.biz.sink.service.impl;

import cn.sinozg.applet.biz.com.model.DevicePropertyMappingCache;
import cn.sinozg.applet.biz.sink.common.TdContext;
import cn.sinozg.applet.biz.sink.common.TdSql;
import cn.sinozg.applet.biz.sink.model.response.TbDevicePropertyResponse;
import cn.sinozg.applet.biz.sink.service.DevicePropertySinkService;
import cn.sinozg.applet.biz.sink.util.TdTableUtil;
import cn.sinozg.applet.biz.sink.vo.request.DevicePropertyHistoryRequest;
import cn.sinozg.applet.biz.sink.vo.request.PropertyInfoAddRequest;
import cn.sinozg.applet.biz.sink.vo.response.PropertyInfoHistoryResponse;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.constant.RedisKey;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.RedisUtil;
import cn.sinozg.applet.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-05 20:27
 */
@Slf4j
@Service
public class DevicePropertySinkServiceImpl implements DevicePropertySinkService {

    @Resource(name = TdContext.JDBC_TEMPLATE)
    private JdbcTemplate template;

    @Override
    public List<PropertyInfoHistoryResponse> devicePropertyHistory(DevicePropertyHistoryRequest params) {
        String tableName = TdTableUtil.prodPropertySuperTableName(params.getProdKey());
        String sqlInfo = String.format(TdSql.PRO_HIS, params.getName().toLowerCase(), tableName, params.getSize());
        List<TbDevicePropertyResponse> list = template.query(sqlInfo, new BeanPropertyRowMapper<>(TbDevicePropertyResponse.class),
                params.getDeviceCode(), params.getStart(), params.getEnd());
        if (CollectionUtils.isNotEmpty(list)) {
            return PojoUtil.copyList(list, PropertyInfoHistoryResponse.class);
        }
        return null;
    }

    @Override
    public void addProperties(PropertyInfoAddRequest params) {
        StringBuilder names = new StringBuilder();
        StringBuilder palace = new StringBuilder();
        List<Object> args = new ArrayList<>();
        args.add(params.getTime());
        Map<String, DevicePropertyMappingCache>  details = params.getDetails();
        if (MapUtils.isNotEmpty(details)) {
            for (Map.Entry<String, DevicePropertyMappingCache> e : details.entrySet()) {
                names.append(e.getKey()).append(BaseConstants.COMMA);
                palace.append("?,");
                args.add(e.getValue().getValue());
            }
        }
        StringUtil.delLast(names);
        StringUtil.delLast(palace);
        String sql = String.format(TdSql.PRO_ADD, TdTableUtil.devicePropertyTableName(params.getDeviceCode()), names,
                TdTableUtil.prodPropertySuperTableName(params.getProdKey()), params.getDeviceCode(), palace);
        template.update(sql, args.toArray());
        RedisUtil.setCacheObject(String.format(RedisKey.DEVICE_PROPERTY_TB, params.getDeviceCode()), true);
    }

    @Override
    public Map<String, Object> lastRecord(String deviceCode) {
        Boolean ex = RedisUtil.getCacheObject(String.format(RedisKey.DEVICE_PROPERTY_TB, deviceCode));
        if (ex == null || !ex) {
            return new HashMap<>(16);
        }
        String sql = String.format(TdSql.PRO_LAST, TdTableUtil.devicePropertyTableName(deviceCode));
        return template.queryForMap(sql, deviceCode);
    }
}
