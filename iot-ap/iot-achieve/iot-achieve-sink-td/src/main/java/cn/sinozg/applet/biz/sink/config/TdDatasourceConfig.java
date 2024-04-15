package cn.sinozg.applet.biz.sink.config;

import cn.sinozg.applet.biz.sink.common.TdContext;
import cn.sinozg.applet.common.properties.AppValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

/**
 * @author xieyubin
 * @Description
 * @Copyright Copyright (c) 2023
 * @since 2023-12-05 21:33
 */
@Configuration
public class TdDatasourceConfig {

    @Resource
    private AppValue app;
//
//    @Bean(name = TdContext.DS)
//    @ConfigurationProperties(prefix = TdContext.PREFIX)
//    public DataSource tdDataSource (){
//        DataSource ds = properties.getDatasource();
//        return ds;
//    }
//
//    @Bean(name = TdContext.SESSION_FACTORY)
//    public SqlSessionFactory tdSqlSessionFactory(@Qualifier(value = TdContext.DS) DataSource ds) throws Exception {
//        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
//        bean.setDataSource(ds);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource(TdContext.MAPPER_LOCATION));
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        PaginationInnerInterceptor page = new PaginationInnerInterceptor();
//        page.setDbType(DbType.TDENGINE);
//        page.setMaxLimit(-1L);
//        page.setOptimizeJoin(true);
//        interceptor.addInnerInterceptor(page);
//        bean.setPlugins(interceptor);
//        return bean.getObject();
//    }

    /**
     * 获取jdbc对象
     * @return jdbc对象
     */
    @Bean(name = TdContext.JDBC_TEMPLATE)
    public JdbcTemplate tdJdbcTemplate() {
        return new JdbcTemplate(app.getTaos().getDatasource());
    }

//    @Bean(name = TdContext.TRANSACTION_MANAGER)
//    public DataSourceTransactionManager tdTransactionManager(@Qualifier(value = TdContext.DS) DataSource ds){
//        return new DataSourceTransactionManager(ds);
//    }
//
//    @Bean(name = TdContext.SESSION_TEMPLATE)
//    public SqlSessionTemplate tdSqlSessionTemplate(@Qualifier(value = TdContext.SESSION_FACTORY) SqlSessionFactory factory){
//        return new SqlSessionTemplate(factory);
//    }
}
