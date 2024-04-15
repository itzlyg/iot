package cn.sinozg.applet.biz.protocol.service;

import cn.sinozg.applet.biz.protocol.entity.ProtocolModule;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModulePageRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleSaveBaseRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleScriptRequest;
import cn.sinozg.applet.biz.protocol.vo.request.ProtocolModuleUpdateRequest;
import cn.sinozg.applet.biz.protocol.vo.response.ProtocolModuleInfoResponse;
import cn.sinozg.applet.biz.protocol.vo.response.ProtocolModulePageResponse;
import cn.sinozg.applet.biz.protocol.vo.response.ProtocolModuleScriptResponse;
import cn.sinozg.applet.biz.protocol.vo.response.SaveJarFileResponse;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* 协议组件信息 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
public interface ProtocolModuleService extends IService<ProtocolModule> {

    /**
    * 新增协议组件信息
    * @param params 协议组件信息
    */
    void createInfo (ProtocolModuleSaveBaseRequest params);

    /**
     * 上传jar包
     * @param request request
     * @return 信息
     */
    SaveJarFileResponse uploadJar(HttpServletRequest request);

    /**
    * 查询详情
    * @param id 主键
    * @return 对象
    */
    ProtocolModuleInfoResponse getInfoById(String id);

    /**
    * 修改协议组件信息
    * @param params 协议组件信息
    */
    void updateInfo(ProtocolModuleUpdateRequest params);

    /**
     * 删除
     * @param id id
     */
    void deleteModule(String id);

    void updateStatus (String id, boolean enable);
    ProtocolModuleScriptResponse getScript(String id);
    void updateScript (ProtocolModuleScriptRequest params);

    /**
     * 查询所有的 协议
     * @param name 名称
     * @return 集合
     */
    List<DictListResponse> moduleList(String name);
    /**
    * 分页查询
    * @param page 分页对象
    * @param params 参数
    * @return 分页集合
    */
    BasePageResponse<List<ProtocolModulePageResponse>> pageInfo(PagingRequest page, ProtocolModulePageRequest params);
}
