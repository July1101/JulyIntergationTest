package com.thg.config.properties;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/8 15:38
 **/


@Data
@ConfigurationProperties(prefix = "integration.datasource")
public class JulyMysqlProperties {

    private boolean enable = false;
    private Map<String, JulyDataSourceProperty> properties;
    private String templatePath;

    @Data
    public static class JulyDataSourceProperty {
        private String url;
        private String username;
        private String password;
        private String driverClassName = "com.mysql.cj.jdbc.Driver";
    }

}
