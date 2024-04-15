package cn.sinozg.applet.biz.protocol.service.impl;

import cn.sinozg.applet.biz.protocol.entity.AnalysisScript;
import cn.sinozg.applet.biz.protocol.mapper.AnalysisScriptMapper;
import cn.sinozg.applet.biz.protocol.mapper.ProtocolModuleMapper;
import cn.sinozg.applet.biz.protocol.service.AnalysisScriptService;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptCreateRequest;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptPageRequest;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptUpdateRequest;
import cn.sinozg.applet.biz.protocol.vo.request.AnalysisScriptUpdateTextRequest;
import cn.sinozg.applet.biz.protocol.vo.request.SelectByNameRequest;
import cn.sinozg.applet.biz.protocol.vo.response.AnalysisScriptPageResponse;
import cn.sinozg.applet.biz.system.vo.response.DictListResponse;
import cn.sinozg.applet.common.constant.BaseConstants;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.base.PagingRequest;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.BizUtil;
import cn.sinozg.applet.common.utils.PageUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.iot.common.enums.ScriptType;
import cn.sinozg.applet.iot.common.utils.ProtocolUtil;
import cn.sinozg.applet.iot.protocol.vo.response.ProtocolAnalysisRegisterResponse;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
* 数据解析脚本 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-20 18:41:52
*/
@Slf4j
@Service
public class AnalysisScriptServiceImpl extends ServiceImpl<AnalysisScriptMapper, AnalysisScript> implements AnalysisScriptService {

    @Resource
    private AnalysisScriptMapper mapper;

    @Resource
    private ProtocolModuleMapper moduleMapper;


    @Override
    public void createInfo(AnalysisScriptCreateRequest params) {
        AnalysisScript entity = PojoUtil.copyBean(params, AnalysisScript.class);
        entity.setScriptType(ScriptType.JS.getCode());
        entity.setDataStatus(Constants.STATUS_00);
        this.save(entity);
    }

    @Override
    public String getScriptById(String id) {
        AnalysisScript entity = this.infoById(id);
        return ProtocolUtil.readScript(entity.getId(), true);
    }

    @Override
    public void deleteScript(String id){
        AnalysisScript entity = this.infoById(id);
        if (Constants.STATUS_01.equals(entity.getDataStatus())) {
            throw new CavException("脚本启用中无法删除！");
        }
        this.removeById(id);
        File file = ProtocolUtil.scriptFile(id, BaseConstants.STATUS_01);
        ProtocolUtil.deleteFile(file);
    }

    @Override
    public void updateStatus (String id, boolean enable){
        AnalysisScript entity = this.infoById(id);
        if (enable && Constants.STATUS_01.equals(entity.getDataStatus())) {
            throw new CavException("解析转化器已经启用！");
        }
        if (!enable && Constants.STATUS_00.equals(entity.getDataStatus())) {
            throw new CavException("解析转化器已经停止！");
        }
        AnalysisScript update = new AnalysisScript();
        update.setId(id);
        if (enable) {
            File scriptFile = ProtocolUtil.scriptFile(id, Constants.STATUS_01);
            if (!scriptFile.exists()) {
                throw new CavException("请先编写解析转化器脚本！");
            }
            update.setDataStatus(Constants.STATUS_01);
        } else {
            // 检查是否被引用
            List<String> moduleIds = moduleMapper.idListByAnalysisId(id);
            if (CollectionUtils.isNotEmpty(moduleIds)) {
                throw new CavException("解析转化器脚本被协议组件引用，请先解除关联！");
            }
            update.setDataStatus(Constants.STATUS_00);
        }
        this.updateById(update);
    }

    @Override
    public void updateInfo(AnalysisScriptUpdateRequest params) {
        this.infoById(params.getId());
        AnalysisScript entity = PojoUtil.copyBean(params, AnalysisScript.class);
        this.updateById(entity);
    }

    @Override
    public void updateScript(AnalysisScriptUpdateTextRequest params) {
        AnalysisScript entity = this.infoById(params.getId());
        if (Constants.STATUS_01.equals(entity.getDataStatus())) {
            throw new CavException("脚本启用中无法更新脚本！");
        }
        ProtocolUtil.writeScript(entity.getId(), true, params.getScript());
    }

    /**
     * 查询所有的 解析脚本
     * @param param 名称
     * @return 集合
     */
    @Override
    public List<DictListResponse> scriptList (SelectByNameRequest param){
        param.setUid(BizUtil.demoUser());
        return mapper.scriptList(param);
    }

    @Override
    public BasePageResponse<List<AnalysisScriptPageResponse>> pageInfo(PagingRequest page, AnalysisScriptPageRequest params) {
        PageUtil<AnalysisScriptPageResponse, AnalysisScriptPageRequest> pu = (p, q) -> mapper.pageInfo(p, q);
        params.setUid(BizUtil.demoUser());
        return pu.page(page, params);
    }

    @Override
    public ProtocolAnalysisRegisterResponse analysisRegister(String id) {
        AnalysisScript entity = mapper.analysisRegister(id);
        if (entity == null) {
            throw new CavException("未找到数据解析脚本！");
        }
        if (Constants.STATUS_00.equals(entity.getDataStatus())) {
            throw new CavException("脚本未启用！");
        }
        ProtocolAnalysisRegisterResponse analysisRegister = new ProtocolAnalysisRegisterResponse();
        analysisRegister.setId(id);
        analysisRegister.setType(entity.getScriptType());
        return analysisRegister;
    }

    /**
     * 查询详情
     * @param id 主键id
     * @return 实体对象
     */
    private AnalysisScript infoById(String id){
        AnalysisScript entity = this.getById(id);
        if (entity == null) {
            throw new CavException("未找到数据解析脚本！");
        }
        return entity;
    }
}
