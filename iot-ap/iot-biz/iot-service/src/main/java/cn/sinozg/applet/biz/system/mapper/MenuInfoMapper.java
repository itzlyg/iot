package cn.sinozg.applet.biz.system.mapper;

import cn.sinozg.applet.biz.system.entity.MenuInfo;
import cn.sinozg.applet.biz.system.vo.request.MenuButtonMaxCode;
import cn.sinozg.applet.biz.system.vo.request.MenuLazyPageRequest;
import cn.sinozg.applet.biz.system.vo.request.MenuTreeRequest;
import cn.sinozg.applet.biz.system.vo.request.UserRoleQueryRequest;
import cn.sinozg.applet.biz.system.vo.response.MenuLazyPageResponse;
import cn.sinozg.applet.biz.system.vo.response.UserSystemMenuResponse;
import cn.sinozg.applet.common.core.model.TreeSelect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* 菜单信息表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:44
*/
public interface MenuInfoMapper extends BaseMapper<MenuInfo> {

    /**
     * 获取用户的菜单或者按钮
     * @param params params
     * @return 结果
     */
    List<UserSystemMenuResponse> userMenuOrButton (UserRoleQueryRequest params);

    /**
     * 懒加载分页
     * @param params params
     * @return 结果
     */
    List<MenuLazyPageResponse> menuLazyPage(MenuLazyPageRequest params);

    /**
     * 菜单信息返回属性结构
     * @param params params
     * @return 结果
     */
    List<TreeSelect> menuTree(MenuTreeRequest params);

    /**
     * 是否存在菜单子节点
     *
     * @param menuCode 菜单 code
     * @return 结果
     */
    int hasChildByMenuId(String menuCode);


    /**
     * 获取到最大的 code
     * @param params 参数
     * @return 最大编码
     */
    String maxCodeNo (MenuButtonMaxCode params);
}
