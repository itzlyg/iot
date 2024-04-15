package cn.sinozg.applet.biz.system.vo.request;

import lombok.Data;

/**
* 资源查询
* @Author: xyb
* @Description:
* @Date: 2023-04-11 下午 10:00
**/
@Data
public class UserRoleQueryRequest {

    /**
     * 资源类型
     **/
    private String tp;
    /**
     * 用户id
     **/
    private String uid;
    /**
     * 用户渠道
     **/
    private String channel;
}
