package com.thg.config.properties;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/17 20:53
 **/


@ConfigurationProperties(prefix = "integration.redis")
@Data
public class JulyRedisProperties {

    private boolean enable = false;
    private List<RedisProperty> properties;
    private List<ConvertProperty> convert;

    @Data
    public static class RedisProperty{
        private String name;
        private String host;
        private int port;
        private String username;
        private String password;
    }

    @Data
    public static class ConvertProperty{
        String name;
        String path;
    }
}
