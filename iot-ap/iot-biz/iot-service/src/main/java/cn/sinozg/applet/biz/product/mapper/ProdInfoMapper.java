package cn.sinozg.applet.biz.product.mapper;

import cn.sinozg.applet.biz.product.entity.ProdInfo;
import cn.sinozg.applet.biz.product.vo.request.ProdInfoPageRequest;
import cn.sinozg.applet.biz.product.vo.response.ProdListResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 产品消息表 Mapper 接口
* @Description
* @Copyright Copyright (c) 2023
* @author zy
* @since 2023-11-27 21:44:29
*/
public interface ProdInfoMapper extends BaseMapper<ProdInfo> {

    /**
    * 根据条件分页查询产品消息表列表
    *
    * @param page 分页信息
    * @param params 产品消息表信息
    * @return 产品消息表信息集合信息
    */
    IPage<ProdInfo> pageInfo(Page<ProdInfo> page, @Param("p") ProdInfoPageRequest params);

    List<ProdListResponse> selectProdList(String name);
}
