package cn.sinozg.applet.biz.protocol.service.impl;

import cn.sinozg.applet.biz.protocol.entity.ProtocolConfig;
import cn.sinozg.applet.biz.protocol.enums.ProtocolType;
import cn.sinozg.applet.biz.protocol.mapper.ProtocolConfigMapper;
import cn.sinozg.applet.biz.protocol.service.ProtocolConfigService;
import cn.sinozg.applet.biz.protocol.vo.module.BaseProtocolConfig;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleSaveBaseRequest;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.JsonUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 协议配置表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Service
public class ProtocolConfigServiceImpl extends ServiceImpl<ProtocolConfigMapper, ProtocolConfig> implements ProtocolConfigService {

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveProtocolConfig(String protocolId, ProtocolModuleSaveBaseRequest params) {
        ProtocolType type = ProtocolType.ofCode(params.getProtocolType());
        Object config;
        try {
            config = PropertyUtils.getProperty(params, type.getFieldName());
        } catch (Exception e) {
            throw new CavException("获取配置失败！");
        }
        if (StringUtils.isNotBlank(protocolId)) {
            this.remove(new LambdaQueryWrapper<ProtocolConfig>().eq(ProtocolConfig::getProtocolId, protocolId));
        }
        ProtocolConfig entity = PojoUtil.copyBean(config, ProtocolConfig.class);
        entity.setProtocolId(protocolId);
        boolean result = this.save(entity);
        if (!result) {
            throw new CavException("保存协议配置失败！");
        }
    }

    @Override
    public BaseProtocolConfig getInfoByProtocolId(String protocolId, String protocolType) {
        List<ProtocolConfig> list = this.list(new LambdaQueryWrapper<ProtocolConfig>()
                .eq(ProtocolConfig::getProtocolId, protocolId));
        boolean isEmpty = CollectionUtils.isEmpty(list) || (CollectionUtils.isNotEmpty(list) && list.size() != 1);
        if (isEmpty) {
            throw new CavException("没有找到对应的协议配置！");
        }
        ProtocolConfig config = list.get(0);
        ProtocolType type = ProtocolType.ofCode(protocolType);
        String otherConfig = config.getOtherConfig();
        BaseProtocolConfig protocolConfig;
        if (StringUtils.isNotBlank(otherConfig)) {
            protocolConfig = JsonUtil.toPojo(otherConfig, type.getConfigBean());
        } else {
            try {
                protocolConfig = PojoUtil.newInstance(type.getConfigBean());
            } catch (Exception ignored) {
                throw new CavException("初始化对象失败！");
            }
        }
        PojoUtil.copyBean(config, protocolConfig);
        return protocolConfig;
    }
}
