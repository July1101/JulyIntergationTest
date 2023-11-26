package com.thg.test.demo.redis;

import java.io.Serializable;
import javax.annotation.Resource;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/2 17:21
 **/


@SpringBootTest(classes = RedisApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MyTest {

//    @Autowired
//    StringRedisTemplate redisTemplate;

    @Resource
    RedisTemplate<String,Object> redisTemplate;


    @Test
    public void test() {

//        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//
////        redisTemplate.setValueSerializer(jsonRedisSerializer);
//
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        Info info = new Info();
        info.setAge(18);
        info.setName("tanhuigen");
        String key = "info3";
        redisTemplate.opsForValue().set(key,info);
        Object res = redisTemplate.opsForValue().get(key);
        System.out.println("res = " + res.getClass());
        System.out.println("info1 = " + res);
        System.out.println("res.toString() = " + res.toString());
    }

    @Data
    public static class Info implements Serializable{
        String name;
        int age;
    }
}
