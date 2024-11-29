package cn.sinozg.applet.biz.notify.model.protocol;

import lombok.Data;

/**
 * 公共的jdbc规范实现的数据库配置信息
 * @author tomsun28
 */
@Data
public class JdbcProtocol {
    /**
     * 对端主机ip或域名
     */
    private String host;
    /**
     * 端口号
     */
    private String port;
    /**
     * 数据库用户名(可选)
     */
    private String username;
    /**
     * 数据库密码(可选)
     */
    private String password;
    /**
     * 数据库
     */
    private String database;
    /**
     * 超时时间
     */
    private String timeout;
    /**
     * 数据库类型 mysql oracle ...
     */
    private String platform;
    /**
     * SQL查询方式： oneRow, multiRow, columns, runScript
     */
    private String queryType;
    /**
     * sql
     */
    private String sql;
    /**
     * 数据库链接url eg: jdbc:mysql://localhost:3306/usthe
     */
    private String url;
}
