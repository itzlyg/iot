package cn.sinozg.applet.biz.system.service.impl;

import cn.sinozg.applet.biz.system.entity.MenuInfo;
import cn.sinozg.applet.biz.system.mapper.MenuInfoMapper;
import cn.sinozg.applet.biz.system.mapper.RoleMenuMapper;
import cn.sinozg.applet.biz.system.service.MenuInfoService;
import cn.sinozg.applet.biz.system.vo.request.MenuAddRequest;
import cn.sinozg.applet.biz.system.vo.request.MenuButtonMaxCode;
import cn.sinozg.applet.biz.system.vo.request.MenuInfoEditRequest;
import cn.sinozg.applet.biz.system.vo.request.MenuLazyPageRequest;
import cn.sinozg.applet.biz.system.vo.request.MenuTreeRequest;
import cn.sinozg.applet.biz.system.vo.request.UserRoleQueryRequest;
import cn.sinozg.applet.biz.system.vo.response.MenuDetailResponse;
import cn.sinozg.applet.biz.system.vo.response.MenuLazyPageResponse;
import cn.sinozg.applet.biz.system.vo.response.UserSystemMenuResponse;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.core.model.TreeSelect;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.PojoUtil;
import cn.sinozg.applet.common.utils.UserUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* 菜单信息表 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:45
*/
@Service
public class MenuInfoServiceImpl extends ServiceImpl<MenuInfoMapper, MenuInfo> implements MenuInfoService {

    @Resource
    private MenuInfoMapper mapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    /** 菜单编码前缀 加 9位数值，三级菜单 每级3位数 M001000000 M001001000 M001001001 */
    private static final String MENU_CODE_PRE = "M";

    /** 按钮 */
    private static final String BUTTON_CODE_PRE = "B";
    @Override
    public void addMenu(MenuAddRequest params) {
        MenuInfo menuInf = PojoUtil.copyBean(params, MenuInfo.class);
        // 一级菜单
        if (StringUtils.isBlank(menuInf.getParentCode())) {
            menuInf.setParentCode(Constants.DICT_ROOT);
        }
        // 默认开启 生效
        menuInf.setDataValid(Constants.STATUS_COMMON);
        String nextCodeNo = nextCodeNo(menuInf.getParentCode(), params.getMenuType());
        menuInf.setMenuCode(nextCodeNo);
        this.save(menuInf);
    }


    @Override
    public void deleteMenuById(String menuId) {
        MenuInfo info = menuInfo(menuId);
        String menuCode = info.getMenuCode();
        int result = mapper.hasChildByMenuId(menuCode);
        if (result > 0) {
            throw new CavException("存在子菜单，不允许删除");
        }
        result = roleMenuMapper.checkMenuExistRole(menuCode);
        if (result > 0) {
            throw new CavException("菜单已分配，不允许删除");
        }
        mapper.deleteById(menuId);
    }

    @Override
    public MenuDetailResponse selectMenuById(String id) {
        MenuInfo info = menuInfo(id);
        return PojoUtil.copyBean(info, MenuDetailResponse.class);
    }

    @Override
    public void editMenu(MenuInfoEditRequest params) {
        MenuInfo menu = PojoUtil.copyBean(params, MenuInfo.class);
        this.updateById(menu);
    }

    @Override
    public List<UserSystemMenuResponse> userSystemMenu(String channel) {
        UserRoleQueryRequest params = new UserRoleQueryRequest();
        params.setTp(Constants.STATUS_01);
        params.setChannel(channel);
        List<UserSystemMenuResponse> result = menus(params);
        return PojoUtil.toTree(result, Constants.DICT_ROOT);
    }

    @Override
    public List<String> userSystemButton(String channel) {
        UserRoleQueryRequest params = new UserRoleQueryRequest();
        params.setTp(Constants.STATUS_02);
        params.setChannel(channel);
        List<UserSystemMenuResponse> list = menus(params);
        return PojoUtil.toList(list, TreeSelect::getId);
    }

    @Override
    public List<MenuLazyPageResponse> menuLazyPage(MenuLazyPageRequest params) {
        if (StringUtils.isBlank(params.getParCode()) && StringUtils.isAllBlank(params.getMenuCode(), params.getMenuName())) {
            params.setParCode(Constants.DICT_ROOT);
        }
        return mapper.menuLazyPage(params);
    }

    @Override
    public List<TreeSelect> menuTree(MenuTreeRequest param) {
        param.setUserId(UserUtil.uid());
        List<TreeSelect> menus = mapper.menuTree(param);
        return PojoUtil.toTree(menus, Constants.DICT_ROOT);
    }
    /**
     * 根据规则获取下一个code
     * @param parentCode 父code
     * @param type 按钮或者菜单
     * @return 下一个code
     */
    private String nextCodeNo(String parentCode, String type){
        // 如果是三级菜单 只能保存按钮 不能保存菜单
        int level = 0;
        // 尾号
        int no = 0;
        int pcode = 0;
        if (!Constants.DICT_ROOT.equals(parentCode)) {
            pcode = Integer.parseInt(StringUtils.substring(parentCode, 1));
            no = pcode;
            int length = 0;
            int steps = 10;
            while (no % steps == 0) {
                no = no / steps;
                length ++;
            }
            level = 3 - (length / 3);
        }
        // 菜单
        if (Constants.STATUS_01.equals(type) && level == 3) {
            throw new CavException("只支持三级菜单！");
        }
        // 按钮
        if (Constants.STATUS_02.equals(type) && level == 0) {
            throw new CavException("按钮不能在根目录！");
        }
        MenuButtonMaxCode params = new MenuButtonMaxCode();
        params.setType(type);
        params.setParCode(parentCode);
        String maxCode = mapper.maxCodeNo(params);
        // 如果为空
        long code;
        long thousand = 1000L;
        // MENU
        if (Constants.STATUS_01.equals(type)) {
            int pow = (int) Math.pow(10, (2 - level) * 3);
            // 如果没有最大值 no * 1000 前进一位
            if (StringUtils.isEmpty(maxCode)) {
                code = no * thousand;
            } else {
                /**
                 * 有的话取最后一位 除去乘积 再加1 再乘以乘积
                 * M001011000-->1011000-->1011--->(1011+1)*1000
                 */
                code = Integer.parseInt(StringUtils.substring(maxCode, 1));
                code = code / pow;
            }
            return String.format("%s%09d", MENU_CODE_PRE , (code + 1) * pow);
        } else {
            // 取出来 * 1000 + 1
            if (StringUtils.isEmpty(maxCode)) {
                code = pcode * thousand;
            } else {
                // 直接加1
                code = Long.parseLong(StringUtils.substring(maxCode, 1));
            }
            return String.format("%s%012d", BUTTON_CODE_PRE, code + 1);
        }
    }

    /**
     * 菜单详情信息
     * @param id
     * @return
     */
    private MenuInfo menuInfo(String id){
        MenuInfo menuInfo = this.getById(id);
        if (menuInfo == null) {
            throw new CavException("菜单信息不存在！");
        }
        return menuInfo;
    }

    private List<UserSystemMenuResponse> menus (UserRoleQueryRequest params) {
        params.setUid(UserUtil.uid());
        return mapper.userMenuOrButton(params);
    }
}
