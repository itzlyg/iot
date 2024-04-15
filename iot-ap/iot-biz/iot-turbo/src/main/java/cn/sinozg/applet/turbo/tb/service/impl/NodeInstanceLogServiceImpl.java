package cn.sinozg.applet.turbo.tb.service.impl;

import cn.sinozg.applet.turbo.tb.entity.NodeInstanceLog;
import cn.sinozg.applet.turbo.tb.mapper.NodeInstanceLogMapper;
import cn.sinozg.applet.turbo.tb.service.NodeInstanceLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* 节点执行记录表 服务实现类
* @Description
* @Copyright Copyright (c) 2024
* @author xieyubin
* @since 2024-01-02 21:12:33
*/
@Service
public class NodeInstanceLogServiceImpl extends ServiceImpl<NodeInstanceLogMapper, NodeInstanceLog> implements NodeInstanceLogService {

    @Resource
    private NodeInstanceLogMapper mapper;

}
