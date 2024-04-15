package cn.sinozg.applet.biz.system.service;

import cn.sinozg.applet.biz.system.entity.DeptInfo;
import cn.sinozg.applet.biz.system.vo.request.DeptInfoCreateRequest;
import cn.sinozg.applet.biz.system.vo.request.DeptInfoPageRequest;
import cn.sinozg.applet.biz.system.vo.request.DeptInfoUpdateRequest;
import cn.sinozg.applet.biz.system.vo.response.DeptInfoDetailResponse;
import cn.sinozg.applet.biz.system.vo.response.DeptInfoPageResponse;
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.model.TreeSelect;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* 部门信息 服务类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-09-06 11:22:14
*/
public interface DeptInfoService extends IService<DeptInfo> {

    /**
    * 新增部门信息
    * @param params 部门信息
    */
    void createInfo (DeptInfoCreateRequest params);

    /**
    * 修改部门信息
    * @param params 部门信息
    */
    void updateInfo(DeptInfoUpdateRequest params);

    /**
     * 删除部门
     * @param deptId 部门id
     */
    String deleteDept(String deptId);

    /**
     * 根据 父id 查询所有的部门
     * @param paterId paterId 父id
     * @param tree 是否为树
     * @return 所有的部门
     */
    List<TreeSelect> listByPaterId(String paterId, boolean tree);

    /**
     * 部门详情
     * @param deptId 部门id
     * @return 详情
     */
    DeptInfoDetailResponse detail(String deptId);

    BasePageResponse<List<DeptInfoPageResponse>> deptPageInfo(BasePageRequest<DeptInfoPageRequest> request);

    List<DeptInfoPageResponse> deptSubInfo(String paterId);
}
