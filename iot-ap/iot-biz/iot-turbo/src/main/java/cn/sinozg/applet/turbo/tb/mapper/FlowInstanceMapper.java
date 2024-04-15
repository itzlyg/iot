package cn.sinozg.applet.turbo.tb.mapper;

import cn.sinozg.applet.turbo.tb.entity.FlowInstance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* 流程执行实例表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
public interface FlowInstanceMapper extends BaseMapper<FlowInstance> {

    FlowInstance selectByFlowInstanceId(String flowInstanceId);

    void updateStatus(@Param("p") FlowInstance params);
}
