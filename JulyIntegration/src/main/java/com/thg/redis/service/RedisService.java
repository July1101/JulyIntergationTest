package com.thg.redis.service;

import com.thg.redis.model.RedisBase;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/24 21:26
 **/
public interface RedisService {

    boolean hit(RedisBase redisBase);

    void initializeRedisBase(RedisBase redisBase);

    void clearData(RedisBase redisBase);

    void insertData(RedisBase redisBase);

    void checkData(RedisBase redisBase);

}
