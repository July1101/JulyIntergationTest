package com.thg.test.demo.redis;

import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/2 21:32
 **/
public class NewTest {


    @Test
    public void name() {
        String host = "localhost";
        int port = 6379;
        RedisStandaloneConfiguration standaloneConfig=new RedisStandaloneConfiguration(host, 6379);
        standaloneConfig.setUsername("default");
        standaloneConfig.setPassword("123456");
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(standaloneConfig);
        jedisConnectionFactory.afterPropertiesSet();
        RedisConnection connection = jedisConnectionFactory.getConnection();
        connection.set("info".getBytes(),"123".getBytes());
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        System.out.println(
            "redisTemplate.opsForValue().get(\"info\") = " + redisTemplate.opsForValue()
                .get("info"));
    }
}
