package com.thg.test.demo.redis;

import com.thg.datasource.EnableDataSourceAutoConfiguration;
import com.thg.test.demo.redis.MyTest.Info;
import lombok.Data;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/2 17:21
 **/
@SpringBootApplication(exclude ={ DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }
}
