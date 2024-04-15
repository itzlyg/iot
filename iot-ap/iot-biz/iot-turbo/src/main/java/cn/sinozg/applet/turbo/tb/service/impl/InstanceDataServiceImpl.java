package cn.sinozg.applet.turbo.tb.service.impl;

import cn.sinozg.applet.turbo.tb.entity.InstanceData;
import cn.sinozg.applet.turbo.tb.mapper.InstanceDataMapper;
import cn.sinozg.applet.turbo.tb.service.InstanceDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* 实例数据表 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
@Service
public class InstanceDataServiceImpl extends ServiceImpl<InstanceDataMapper, InstanceData> implements InstanceDataService {

    @Resource
    private InstanceDataMapper mapper;


    @Override
    public InstanceData selectByIns(String flowInstanceId, String instanceDataId) {
        InstanceData params = new InstanceData();
        params.setInstanceDataId(instanceDataId);
        params.setFlowInstanceId(flowInstanceId);
        return mapper.selectRecent(params);
    }

    @Override
    public InstanceData selectRecent(String flowInstanceId) {
        return selectByIns(flowInstanceId, null);
    }
}
