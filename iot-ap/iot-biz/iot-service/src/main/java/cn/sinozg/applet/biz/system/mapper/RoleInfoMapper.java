package cn.sinozg.applet.biz.system.mapper;

import cn.sinozg.applet.biz.system.entity.RoleInfo;
import cn.sinozg.applet.biz.system.vo.request.RoleInfoPageRequest;
import cn.sinozg.applet.biz.system.vo.response.RoleInfoPageResponse;
import cn.sinozg.applet.biz.system.vo.response.UserRoleResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 角色信息表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author xyb
* @since 2023-04-11 20:40:45
*/
public interface RoleInfoMapper extends BaseMapper<RoleInfo> {

    /**
     * 根据role key查找对应的name
     * @param list key 集合
     * @return m c
     */
    List<RoleInfo> roleKeyNameMap(List<String> list);

    /**
     * 角色分页
     * @param page 分页
     * @param request 参数
     * @return 分页
     */
    IPage<RoleInfoPageResponse> userRolePage(Page<RoleInfoPageResponse> page, @Param("p") RoleInfoPageRequest request);

    /**
     * 用户角色信息
     * @param request 参数
     * @return 分页
     */
    List<UserRoleResponse> userRoles(@Param("p") RoleInfoPageRequest request);
}
