package cn.sinozg.applet.turbo.tb.mapper;

import cn.sinozg.applet.turbo.tb.entity.InstanceData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* 实例数据表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
public interface InstanceDataMapper extends BaseMapper<InstanceData> {

    InstanceData selectRecent (@Param("p") InstanceData params);
}
