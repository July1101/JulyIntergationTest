package com.thg.redis.config;

import com.alibaba.fastjson.JSON;
import com.thg.TestContext;
import com.thg.redis.model.RedisDataType;
import com.thg.redis.model.RedisReport;
import com.thg.utils.ReportUtils;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.CollectionUtils;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/4 13:42
 **/
public class RedisOpsMethodInterceptor implements MethodInterceptor {

    RedisDataType redisDataType;
    List<String> methodFilter = null;

    public RedisOpsMethodInterceptor(RedisDataType redisDataType) {
        this.redisDataType = redisDataType;
    }

    public RedisOpsMethodInterceptor(RedisDataType redisDataType, List<String> methodFilter) {
        this.redisDataType = redisDataType;
        this.methodFilter = methodFilter;
    }

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        if(CollectionUtils.isEmpty(methodFilter)||methodFilter.contains(methodName)){
            String args = JSON.toJSONString(invocation.getArguments());
            long start = System.currentTimeMillis();
            Object res = invocation.proceed();
            long end = System.currentTimeMillis();
            RedisReport redisReport = new RedisReport(TestContext.casePace,redisDataType,methodName, args,
                JSON.toJSONString(res),
                end - start);
            ReportUtils.addRedisReport(redisReport);
            return res;
        }
        return invocation.proceed();
    }
}
