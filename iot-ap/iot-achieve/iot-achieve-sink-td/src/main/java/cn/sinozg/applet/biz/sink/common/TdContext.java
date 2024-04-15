package cn.sinozg.applet.biz.sink.common;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-05 21:35
 */
public class TdContext {

    public static final String SESSION_FACTORY = "tdSqlSessionFactory";

    public static final String DS = "tdDataSource";

    public static final String JDBC_TEMPLATE = "tdJdbcTemplate";

    public static final String SESSION_TEMPLATE = "tdSqlSessionTemplate";

    public static final String MAPPER_LOCATION = "classpath*:tdmapper/**/*Mapper.xml";

    public static final String PREFIX = "taos";

    public static final String TRANSACTION_MANAGER = "tdTransactionManager";

    public static final String DEVICE_CODE = "device_code";

    public static final int RUST_CODE_SUCCESS = 0;
    public static final int RUST_CODE_TB_NOT_EXIST = 866;

    public static final String TIMESTAMP = "ts";
}
