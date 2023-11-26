package com.thg.datasource.property;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * Created by tanhuigen
 * Date 2022-09-03
 * Description
 */

@EnableConfigurationProperties
@ConditionalOnClass(HikariDataSource.class)
@ConfigurationProperties(prefix = "mysql.datasource")
@Data
public class DataSourceProperties1 {

    private boolean enabled = true;

    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    private Long connectionTimeout = 300L;

    private Long maxLifetime;

    private Long idleTimeout;

    private List<DataSourceProperty> properties;


}
