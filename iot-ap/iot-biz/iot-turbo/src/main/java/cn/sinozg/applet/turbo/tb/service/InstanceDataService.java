package cn.sinozg.applet.turbo.tb.service;

import cn.sinozg.applet.turbo.tb.entity.InstanceData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* 实例数据表 服务类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
public interface InstanceDataService extends IService<InstanceData> {


    InstanceData selectByIns(String flowInstanceId, String instanceDataId);

    InstanceData selectRecent (String flowInstanceId);
}
