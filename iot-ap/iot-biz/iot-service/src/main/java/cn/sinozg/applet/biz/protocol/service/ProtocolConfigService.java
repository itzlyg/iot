package cn.sinozg.applet.biz.protocol.service;

import cn.sinozg.applet.biz.protocol.entity.ProtocolConfig;
import cn.sinozg.applet.biz.protocol.vo.module.BaseProtocolConfig;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleSaveBaseRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 协议配置表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
public interface ProtocolConfigService extends IService<ProtocolConfig> {

    /**
    * 新增协议配置表
     * @param protocolId protocolId
    * @param params 协议配置表
    */
    void saveProtocolConfig(String protocolId, ProtocolModuleSaveBaseRequest params);

    /**
    * 查询详情
    * @param protocolId 协议id
     * @param protocolType 协议类型
    * @return 对象
    */
    BaseProtocolConfig getInfoByProtocolId(String protocolId, String protocolType);
}
