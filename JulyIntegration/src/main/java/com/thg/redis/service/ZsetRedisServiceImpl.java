package com.thg.redis.service;

import com.fasterxml.jackson.databind.JavaType;
import com.thg.redis.config.RedisConfigFactory;
import com.thg.redis.model.RedisBase;
import com.thg.redis.model.RedisDataType;
import com.thg.utils.AssertUtils;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/24 21:58
 **/

@Service
@ConditionalOnBean(RedisConfigFactory.class)
public class ZsetRedisServiceImpl extends AbstractRedisService {


    protected ZsetRedisServiceImpl(RedisConfigFactory redisConfigFactory) {
        super(redisConfigFactory);
    }

    @Override
    public boolean hit(RedisBase redisBase) {
        return redisBase.getZset() != null;
    }

    @Override
    public void initializeRedisBase(RedisBase redisBase) {
        redisBase.setType(RedisDataType.ZSET);
        JavaType javaType = getJavaType(redisBase);
        Set<TypedTuple<Object>> tuples = redisBase.getZset().stream().map(
            tuple -> {
                Object afterValue = innerConvertJsonNode(tuple.getValue(), javaType);
                return (TypedTuple<Object>) new DefaultTypedTuple<Object>(afterValue,
                    tuple.getScore());
            }).collect(Collectors.toSet());
        redisBase.setZset(tuples);
    }

    @Override
    public void insertData(RedisBase redisBase) {
        getRedisTemplate(redisBase).opsForZSet().add(redisBase.getKey(),redisBase.getZset());
    }

    @Override
    public void checkData(RedisBase redisBase) {
        Set<TypedTuple<Object>> expect = redisBase.getZset();
        Set<TypedTuple<Object>> actual = getRedisTemplate(redisBase).opsForZSet()
            .rangeWithScores(redisBase.getKey(), 0, -1);
        AssertUtils.assertEquals(expect,actual,true);
    }

}
