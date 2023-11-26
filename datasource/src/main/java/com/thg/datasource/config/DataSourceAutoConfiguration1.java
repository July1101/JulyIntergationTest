package com.thg.datasource.config;

import com.thg.datasource.property.DataSourceProperties1;
import com.thg.datasource.routing.DataSourceRouting;
import com.thg.datasource.property.DataSourceProperty;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanhuigen
 * Date 2022-09-03
 * Description
 */


@Configuration
@Slf4j
@EnableConfigurationProperties({DataSourceProperties1.class})
public class DataSourceAutoConfiguration1 implements DisposableBean {


    @Autowired
    private DataSourceProperties1 dataSourceProperties1;


    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "mysql.datasource", name = "enabled", havingValue = "true", matchIfMissing = false)
    public DataSource dataSource1(){
        log.info("mysql.datasource begin initialize");
        return buildDataSource();
    }


    private DataSource buildDataSource(){
        DataSourceRouting dataSourceRouting = new DataSourceRouting();
        List<DataSourceProperty> properties = dataSourceProperties1.getProperties();
        Map<Object,Object> sourceMap = new HashMap<>();
        String primaryDataSourceName = null;

        if(CollectionUtils.isEmpty(properties)){
            throw new NullPointerException("the datasource list is empty!");
        }
        for (DataSourceProperty property : properties) {
            DataSource dataSource = hikariDataSourceBuilder(property);
            sourceMap.put(property.getName(), dataSource);
            if(primaryDataSourceName==null||property.isPrimary()){
                primaryDataSourceName =  property.getName();
            }
        }
        dataSourceRouting.setTargetDataSources(sourceMap);
        dataSourceRouting.setDefaultTargetDataSource(sourceMap.get(primaryDataSourceName));
        return dataSourceRouting;
    }

    private DataSource hikariDataSourceBuilder(DataSourceProperty dataSourceProperty){
        HikariConfig config = new HikariConfig();
        config.setRegisterMbeans(false);
        config.setDriverClassName(dataSourceProperties1.getDriverClassName());
        config.setJdbcUrl(dataSourceProperty.getUrl());
        config.setUsername(dataSourceProperty.getUsername());
        config.setPassword(dataSourceProperty.getPassword());
        config.setMaximumPoolSize(dataSourceProperty.getMaximumPoolSize());
        if(dataSourceProperty.getMinimumIdle()!=null){
            config.setMinimumIdle(dataSourceProperty.getMinimumIdle());
        }
        if(dataSourceProperties1.getMaxLifetime()!=null){
            config.setMaxLifetime(dataSourceProperties1.getMaxLifetime());
        }
        if(dataSourceProperties1.getConnectionTimeout()!=null){
            config.setConnectionTimeout(dataSourceProperties1.getConnectionTimeout());
        }
        return new HikariDataSource(config);
    }

    @Override
    public void destroy() throws Exception {

    }
}
