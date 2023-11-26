package com.thg.redis.model;

import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/2 20:31
 **/
public interface JulyRedisSerializer<T> extends RedisSerializer<T> {
    String className();
}
