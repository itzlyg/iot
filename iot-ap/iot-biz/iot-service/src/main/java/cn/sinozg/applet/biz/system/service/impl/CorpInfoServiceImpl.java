package cn.sinozg.applet.biz.system.service.impl;

import cn.sinozg.applet.biz.system.entity.CorpInfo;
import cn.sinozg.applet.biz.system.mapper.CorpInfoMapper;
import cn.sinozg.applet.biz.system.service.CorpInfoService;
import cn.sinozg.applet.biz.system.vo.request.CropCreateRequest;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.core.model.DictDataVo;
import cn.sinozg.applet.common.utils.BizUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* 租户消息表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-11-09 16:04:37
*/
@Service
public class CorpInfoServiceImpl extends ServiceImpl<CorpInfoMapper, CorpInfo> implements CorpInfoService {

    @Resource
    private CorpInfoMapper mapper;

    @Override
    public void createCrop(CropCreateRequest params) {
        CorpInfo corp = PojoUtil.copyBean(params, CorpInfo.class);
        corp.setDataStatus(Constants.STATUS_01);
        String tenantId = BizUtil.randomCode(8, this, CorpInfo::getTenantId);
        corp.setTenantId(tenantId);
        this.save(corp);
    }

    @Override
    public List<DictDataVo> corpList() {
        return mapper.corpList();
    }
}
