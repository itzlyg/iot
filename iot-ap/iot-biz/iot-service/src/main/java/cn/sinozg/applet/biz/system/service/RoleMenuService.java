package cn.sinozg.applet.biz.system.service;

import cn.sinozg.applet.biz.system.entity.RoleMenu;
import cn.sinozg.applet.biz.system.vo.request.RoleMenuQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 角色菜单表 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:45
*/
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 查询角色已有的菜单
     * @param params 参数
     * @return 菜单id
     */
    List<String> roleCheckedMenu (RoleMenuQueryRequest params);


    /**
     * 删除对应角色的菜单
     * @param code
     * @param channel
     * @return
     */
    boolean deleteResource (String code, String channel);
}
