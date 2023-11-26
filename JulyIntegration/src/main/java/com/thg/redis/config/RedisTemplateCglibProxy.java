package com.thg.redis.config;

import com.thg.redis.model.RedisDataType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/5 20:30
 **/
public class RedisTemplateCglibProxy implements MethodInterceptor {


    private final List<String> defaultFilterMethod = Arrays.asList("hasKey","delete");

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> opsForValue;
    private final ListOperations<String, Object> opsForList;
    private final HashOperations<String, Object, Object> opsForHash;
    private final SetOperations<String, Object> opsForSet;
    private final ZSetOperations<String, Object> opsForZset;

    private RedisTemplateCglibProxy(RedisTemplate<String, Object> redisTemplate) {
        this.opsForValue = (ValueOperations<String, Object>)
            getProxy(redisTemplate.opsForValue(), new RedisOpsMethodInterceptor(RedisDataType.STRING));
        this.opsForList = (ListOperations<String, Object>)
            getProxy(redisTemplate.opsForList(), new RedisOpsMethodInterceptor(RedisDataType.LIST));
        this.opsForSet = (SetOperations<String, Object>)
            getProxy(redisTemplate.opsForSet(), new RedisOpsMethodInterceptor(RedisDataType.SET));
        this.opsForZset = (ZSetOperations<String, Object>)
            getProxy(redisTemplate.opsForZSet(),new RedisOpsMethodInterceptor(RedisDataType.ZSET));
        this.opsForHash = (HashOperations<String, Object, Object>)
            getProxy(redisTemplate.opsForHash(),new RedisOpsMethodInterceptor(RedisDataType.HASH));
        this.redisTemplate = (RedisTemplate<String, Object>)
            getProxy(redisTemplate,new RedisOpsMethodInterceptor(RedisDataType.DEFAULT,defaultFilterMethod));
    }

    public static RedisTemplate<String,Object> newInstance(RedisTemplate<String,Object> originInstance) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RedisTemplate.class);
        RedisTemplateCglibProxy redisTemplateCglibProxy = new RedisTemplateCglibProxy(originInstance);
        enhancer.setCallback(redisTemplateCglibProxy);
        return (RedisTemplate<String,Object>)enhancer.create();
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
        throws Throwable {
        switch (method.getName()) {
            case "opsForValue":
                return opsForValue;
            case "opsForList":
                return opsForList;
            case "opsForSet":
                return opsForSet;
            case "opsForZSet":
                return opsForZset;
            case "opsForHash":
                return opsForHash;
            default:
                return method.invoke(redisTemplate,objects);
        }
    }

    private Object getProxy(Object instance, RedisOpsMethodInterceptor methodInterceptor) {
        ProxyFactory proxyFactory = new ProxyFactory(instance);
        proxyFactory.setTarget(instance);
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvice(methodInterceptor);
        return proxyFactory.getProxy();
    }
}
