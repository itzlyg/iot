package cn.sinozg.applet.biz.protocol.mapper;

import cn.sinozg.applet.biz.protocol.entity.AnalysisScript;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptPageRequest;
import cn.sinozg.applet.biz.protocol.vo.request.SelectByNameRequest;
import cn.sinozg.applet.biz.protocol.vo.response.AnalysisScriptPageResponse;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.common.constant.Constants;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 数据解析脚本 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
public interface AnalysisScriptMapper extends BaseMapper<AnalysisScript> {

    /**
    * 根据条件分页查询数据解析脚本列表
    *
    * @param page 分页信息
    * @param params 数据解析脚本信息
    * @return 数据解析脚本信息集合信息
    */
    IPage<AnalysisScriptPageResponse> pageInfo(Page<AnalysisScriptPageResponse> page, @Param("p") AnalysisScriptPageRequest params);

    /**
     * 下拉 用于选择
     * @param scriptName 名称
     * @return 集合
     */
    List<DictListResponse> scriptList (@Param("p") SelectByNameRequest scriptName);

    /**
     * 注册脚本信息
     * @param id id
     * @return 信息
     */
    @InterceptorIgnore(tenantLine = Constants.IGNORE)
    AnalysisScript analysisRegister(String id);
}
