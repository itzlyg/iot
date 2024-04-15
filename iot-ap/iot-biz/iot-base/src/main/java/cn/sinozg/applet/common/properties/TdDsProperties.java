package cn.sinozg.applet.common.properties;

import lombok.Data;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-06 17:50
 */
@Data
public class TdDsProperties {
    /**
     * 每一个数据源
     */
    private String restUrl;
    private DriverManagerDataSource datasource;
}
