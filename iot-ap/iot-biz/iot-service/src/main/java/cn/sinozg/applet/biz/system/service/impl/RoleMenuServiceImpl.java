package cn.sinozg.applet.biz.system.service.impl;

import cn.sinozg.applet.biz.system.entity.RoleMenu;
import cn.sinozg.applet.biz.system.mapper.RoleMenuMapper;
import cn.sinozg.applet.biz.system.service.RoleMenuService;
import cn.sinozg.applet.biz.system.vo.request.RoleMenuQueryRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* 角色菜单表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:45
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Resource
    private RoleMenuMapper mapper;

    @Override
    public List<String> roleCheckedMenu(RoleMenuQueryRequest params) {
        return mapper.roleCheckedMenu(params);
    }

    @Override
    public boolean deleteResource(String code, String channel) {
        return mapper.deleteResource(code, channel);
    }
}
