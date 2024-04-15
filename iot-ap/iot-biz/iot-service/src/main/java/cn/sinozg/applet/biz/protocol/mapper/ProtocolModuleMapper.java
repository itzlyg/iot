package cn.sinozg.applet.biz.protocol.mapper;

import cn.sinozg.applet.biz.protocol.entity.ProtocolModule;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModulePageRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleSelectRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleStateTypeRequest;
import cn.sinozg.applet.biz.protocol.vo.response.ProtocolModulePageResponse;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolModuleRegisterResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 协议组件信息 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
public interface ProtocolModuleMapper extends BaseMapper<ProtocolModule> {

    /**
    * 根据条件分页查询协议组件信息列表
    *
    * @param page 分页信息
    * @param params 协议组件信息信息
    * @return 协议组件信息信息集合信息
    */
    IPage<ProtocolModulePageResponse> pageInfo(Page<ProtocolModulePageResponse> page, @Param("p") ProtocolModulePageRequest params);


    /**
     * 下拉 用于选择
     * @param protocolName 名称
     * @return 集合
     */
    List<DictListResponse> moduleList (@Param("p") ProtocolModuleSelectRequest params);

    /**
     * 查找 引用了脚本的协议
     * @param analysisId 解析脚本id
     * @return id集合
     */
    List<String> idListByAnalysisId(String analysisId);

    /**
     * 查询协议信息去注册
     * @param params 参数
     * @return 协议
     */
    @InterceptorIgnore(tenantLine = Constants.IGNORE)
    List<ProtocolModuleRegisterResponse> protocolModuleStateType(@Param("p") ProtocolModuleStateTypeRequest params);
}
