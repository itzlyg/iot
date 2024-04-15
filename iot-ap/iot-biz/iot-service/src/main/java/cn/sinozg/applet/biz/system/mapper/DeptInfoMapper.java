package cn.sinozg.applet.biz.system.mapper;

import cn.sinozg.applet.biz.system.entity.DeptInfo;
import cn.sinozg.applet.biz.system.vo.request.DeptInfoPageRequest;
import cn.sinozg.applet.biz.system.vo.response.DeptInfoDetailResponse;
import cn.sinozg.applet.biz.system.vo.response.DeptInfoPageResponse;
import cn.sinozg.applet.common.constant.Constants;
import cn.sinozg.applet.common.core.model.TreeSelect;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 部门信息 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author xieyubin
* @since 2023-09-06 11:22:14
*/
public interface DeptInfoMapper extends BaseMapper<DeptInfo> {

    /**
     * 根据父id 查找数据
     * @param paterId 父id
     * @return 数据
     */
    @InterceptorIgnore(tenantLine = Constants.IGNORE)
    List<TreeSelect> listByPaterId(String paterId);

    /**
     * 插叙部门详情
     * @param id id
     * @return 详情
     */
    DeptInfoDetailResponse deptDetail (String id);


    /**
     * 查询部门分页
     * @param page 分页
     * @param params 用户请求参数
     * @return 部门分页记录集合
     */
    IPage<DeptInfoPageResponse> deptPage(Page<DeptInfoPageResponse> page, @Param("p") DeptInfoPageRequest params);

    /**
     * 根据父部门查询子部门集合
     * @param params 参数
     * @return 集合
     */
    List<DeptInfoPageResponse> pageSub(@Param("p") DeptInfoPageRequest params);
}
