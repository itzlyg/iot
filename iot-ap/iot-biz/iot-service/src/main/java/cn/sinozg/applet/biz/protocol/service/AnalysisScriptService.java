package cn.sinozg.applet.biz.protocol.service;

import cn.sinozg.applet.biz.protocol.entity.AnalysisScript;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptCreateRequest;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptPageRequest;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptUpdateRequest;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptUpdateTextRequest;
import cn.sinozg.applet.biz.protocol.vo.request.SelectByNameRequest;
import cn.sinozg.applet.biz.protocol.vo.response.AnalysisScriptPageResponse;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolAnalysisRegisterResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 数据解析脚本 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
public interface AnalysisScriptService extends IService<AnalysisScript> {

    /**
    * 新增数据解析脚本
    * @param params 数据解析脚本
    */
    void createInfo (AnalysisScriptCreateRequest params);

    /**
     * 删除脚本
     * @param id id
     */
    void deleteScript(String id);

    /**
    * 查询详情
    * @param id 主键
    * @return 对象
    */
    String getScriptById(String id);

    /**
    * 修改数据解析脚本
    * @param params 数据解析脚本
    */
    void updateInfo(AnalysisScriptUpdateRequest params);

    /**
     * 更新脚本信息
     * @param params 参数
     */
    void updateScript(AnalysisScriptUpdateTextRequest params);

    void updateStatus (String id, boolean enable);

    /**
     * 查询所有的 解析脚本
     * @param name 名称
     * @return 集合
     */
    List<DictListResponse> scriptList (SelectByNameRequest name);

    /**
    * 分页查询
    * @param page 分页对象
    * @param params 参数
    * @return 分页集合
    */
    BasePageResponse<List<AnalysisScriptPageResponse>> pageInfo(PagingRequest page, AnalysisScriptPageRequest params);

    /**
     * 查询脚本信息 去注册
     * @param id id
     * @return 信息
     */
    ProtocolAnalysisRegisterResponse analysisRegister(String id);
}
