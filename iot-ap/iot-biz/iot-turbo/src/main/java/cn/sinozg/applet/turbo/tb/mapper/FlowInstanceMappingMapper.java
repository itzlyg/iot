package cn.sinozg.applet.turbo.tb.mapper;

import cn.sinozg.applet.turbo.tb.entity.FlowInstanceMapping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 父子流程实例映射表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
public interface FlowInstanceMappingMapper extends BaseMapper<FlowInstanceMapping> {

    List<FlowInstanceMapping> selectFlowInstanceMappingList(@Param("p") FlowInstanceMapping params);

    int updateType(@Param("p") FlowInstanceMapping params);
}
