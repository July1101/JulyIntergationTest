package com.thg.redis.service;

import com.fasterxml.jackson.databind.JavaType;
import com.thg.redis.config.RedisConfigFactory;
import com.thg.redis.model.RedisBase;
import com.thg.redis.model.RedisDataType;
import com.thg.utils.AssertUtils;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/24 21:44
 **/
@Service
@ConditionalOnBean(RedisConfigFactory.class)
public class ListRedisServiceImpl extends AbstractRedisService {


    protected ListRedisServiceImpl(RedisConfigFactory redisConfigFactory) {
        super(redisConfigFactory);
    }

    @Override
    public boolean hit(RedisBase redisBase) {
        return redisBase.getList() != null;
    }

    @Override
    public void initializeRedisBase(RedisBase redisBase) {
        redisBase.setType(RedisDataType.LIST);
        JavaType javaType = getJavaType(redisBase);
        redisBase.getList().replaceAll(value -> innerConvertJsonNode(value, javaType));
    }

    @Override
    public void insertData(RedisBase redisBase) {
        getRedisTemplate(redisBase).opsForList().rightPushAll(redisBase.getKey(),redisBase.getList());
    }

    @Override
    public void checkData(RedisBase redisBase) {
        List<Object> actual = getRedisTemplate(redisBase).opsForList().range(redisBase.getKey(),0,-1);
        List<Object> expect = redisBase.getList();
        AssertUtils.assertEquals(expect,actual,false);
    }
}
