package com.customer.apims.config.spring;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;
import org.apache.ibatis.annotations.Mapper;
import org.ezdevgroup.ezframework.web.GlobalProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * @author Son
 * !! 1)  Sever Request -> Dev Log Write
 * !! 2) Server DataBase Lookup -> db address Findding !
 *
 */
@ImportResource("classpath:config/spring/context-*.xml")
@Configuration
@MapperScan(basePackages = "com.customer", annotationClass = Mapper.class, sqlSessionFactoryRef = "sqlSessionFactoryBean")
public class ApplicationContext {

    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    @Value("#{global['server.type'}")
    private String serverType;

    @Bean
    @Resource(name="jdbc/mariaDB")
    public DataSource dataSource(){
        DataSource dataSource = null;

        JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);


        try {
            dataSource = dsLookup.getDataSource("java:comp/env/jdbc/mariaDB");
            log.debug("dataSource : " + dataSource);
        }catch (Exception e){
            log.error("Create Bean ! Not Found Ur databaseSource !");
        }

        if(dataSource == null) {
            log.error(" Ur DataSources in NULL !!");
        }else {

            if(GlobalProperties.LOCAL.equals(serverType) || GlobalProperties.DEV.equals(serverType)){
                log.debug(" Dev || Local Log4JDBC Write ! Ur Console and logback.txt !");

                try {
                    Log4JdbcCustomFormatter log4JdbcCustomFormatter = new Log4JdbcCustomFormatter();
                    log4JdbcCustomFormatter.setLoggingType(LoggingType.MULTI_LINE);
                    log4JdbcCustomFormatter.setSqlPrefix("SQL     :\n\t\t");

                    Log4jdbcProxyDataSource log4jdbcProxyDataSource = new Log4jdbcProxyDataSource(dataSource);
                    log4jdbcProxyDataSource.setLogFormatter(log4JdbcCustomFormatter);

                    dataSource = log4jdbcProxyDataSource;
                } catch(Exception e){
                    log.error(e.toString());
                }

            }
        }
        return dataSource;
    }
}
