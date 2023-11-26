package com.thg.redis.service;

import com.fasterxml.jackson.databind.JavaType;
import com.thg.redis.config.RedisConfigFactory;
import com.thg.redis.model.RedisBase;
import com.thg.redis.model.RedisDataType;
import com.thg.utils.AssertUtils;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/24 21:48
 **/
@Service
@ConditionalOnBean(RedisConfigFactory.class)
public class HashRedisServiceImpl extends AbstractRedisService {


    protected HashRedisServiceImpl(RedisConfigFactory redisConfigFactory) {
        super(redisConfigFactory);
    }

    @Override
    public boolean hit(RedisBase redisBase) {
        return redisBase.getHash() != null;
    }

    @Override
    public void initializeRedisBase(RedisBase redisBase) {
        redisBase.setType(RedisDataType.HASH);
        JavaType javaType = getJavaType(redisBase);
        redisBase.getHash()
            .replaceAll((key,value)->innerConvertJsonNode(value,javaType));
    }

    @Override
    public void insertData(RedisBase redisBase) {
        HashOperations<String, String, Object> hashOperations = getRedisTemplate(redisBase).opsForHash();
        hashOperations.putAll(redisBase.getKey(),redisBase.getHash());
    }

    @Override
    public void checkData(RedisBase redisBase) {
        HashOperations<String, String, Object> hashOperations = getRedisTemplate(redisBase).opsForHash();
        Map<String,Object> actual = hashOperations.entries(redisBase.getKey());
        Map<String, Object> expect = redisBase.getHash();
        AssertUtils.assertEquals(expect,actual,false);
    }
}
