package cn.sinozg.applet.biz.system.service.impl;

import cn.sinozg.applet.biz.system.entity.DeptInfo;
import cn.sinozg.applet.biz.system.entity.UserInfo;
import cn.sinozg.applet.biz.system.mapper.DeptInfoMapper;
import cn.sinozg.applet.biz.system.service.DeptInfoService;
import cn.sinozg.applet.biz.system.service.UserInfoService;
import cn.sinozg.applet.biz.system.vo.request.DeptInfoCreateRequest;
import cn.sinozg.applet.biz.system.vo.request.DeptInfoPageRequest;
import cn.sinozg.applet.biz.system.vo.request.DeptInfoUpdateRequest;
import cn.sinozg.applet.biz.system.vo.response.DeptInfoDetailResponse;
import cn.sinozg.applet.biz.system.vo.response.DeptInfoPageResponse;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.core.base.BasePageRequest;
import cn.sinozg.applet.common.core.base.BasePageResponse;
import cn.sinozg.applet.common.core.model.TreeSelect;
import cn.sinozg.applet.common.exception.CavException;
import cn.sinozg.applet.common.utils.MsgUtil;
import cn.sinozg.applet.common.utils.PageUtil;
import cn.sinozg.applet.common.utils.PojoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* 部门信息 服务实现类
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-09-06 11:22:14
*/
@Service
public class DeptInfoServiceImpl extends ServiceImpl<DeptInfoMapper, DeptInfo> implements DeptInfoService {

    @Resource
    private DeptInfoMapper mapper;


    @Resource
    private UserInfoService userInfoService;

    @Override
    public void createInfo(DeptInfoCreateRequest params) {
        DeptInfo entity = PojoUtil.copyBean(params, DeptInfo.class);
        if (StringUtils.isBlank(params.getPaterId())) {
            entity.setPaterId(Constants.DICT_ROOT);
        }
        this.save(entity);
    }


    @Override
    public void updateInfo(DeptInfoUpdateRequest params) {
        deptInfo(params.getId());
        DeptInfo entity = PojoUtil.copyBean(params, DeptInfo.class);
        this.updateById(entity);
    }

    @Override
    public String deleteDept(String deptId) {
        DeptInfoDetailResponse di = this.deptInfo(deptId);
        List<TreeSelect> list = listByPaterId(deptId, false);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new CavException("部门存在子部门！");
        }
        List<UserInfo> users = userInfoService.userByDeptId(deptId);
        if (CollectionUtils.isNotEmpty(users)) {
            throw new CavException("部门下存在员工，请先处理！");
        }
        this.removeById(deptId);
        return di.getPaterId();
    }

    @Override
    public List<TreeSelect> listByPaterId(String paterId, boolean tree){
        if (StringUtils.isBlank(paterId)) {
            paterId = Constants.DICT_ROOT;
        }
        List<TreeSelect> list = mapper.listByPaterId(paterId);
        if (tree && CollectionUtils.isNotEmpty(list)) {
            List<TreeSelect> deptTree = PojoUtil.toTree(list, paterId);
            checked(deptTree);
            return deptTree;
        }
        return list;
    }

    @Override
    public DeptInfoDetailResponse detail(String deptId) {
        return deptInfo(deptId);
    }

    @Override
    public BasePageResponse<List<DeptInfoPageResponse>> deptPageInfo(BasePageRequest<DeptInfoPageRequest> request) {
        DeptInfoPageRequest params = MsgUtil.params(request);
        params.setPaterId(Constants.DICT_ROOT);
        PageUtil<DeptInfoPageResponse, DeptInfoPageRequest> pu = (p, q) -> mapper.deptPage(p, q);
        return pu.page(request.getPage(), params);
    }

    @Override
    public List<DeptInfoPageResponse> deptSubInfo(String paterId){
        DeptInfoPageRequest params = new DeptInfoPageRequest();
        params.setPaterId(paterId);
        return mapper.pageSub(params);
    }

    private DeptInfoDetailResponse deptInfo (String deptId){
        DeptInfoDetailResponse info = mapper.deptDetail(deptId);
        if (info == null) {
            throw new CavException("未找到对应的部门信息！");
        }
        return info;
    }

    private void checked(List<? extends TreeSelect> tree){
        if (CollectionUtils.isNotEmpty(tree)) {
            for (TreeSelect t : tree) {
                if (!t.isChecked() && CollectionUtils.isNotEmpty(t.getChildren())) {
                    t.setChecked(true);
                }
                checked(t.getChildren());
            }
        }
    }
}
