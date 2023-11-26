package com.thg.mysql.config;

import com.alibaba.fastjson.JSON;
import com.thg.report.mysql.SqlOperation;
import com.thg.utils.ReportUtils;
import com.thg.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/14 13:41
 **/
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class MyBatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        String sql = SqlUtils.showSql(mappedStatement.getConfiguration(),
            mappedStatement.getBoundSql(parameterObject));
        SqlOperation sqlOperation = ReportUtils.registerSql(sql);
        long start = System.currentTimeMillis();
        Object res = null;
        try {
            res = invocation.proceed();
            return res;
        } finally {
            long end = System.currentTimeMillis();
            if (res != null) {
                sqlOperation.setRes(JSON.toJSONString(res));
            }
            sqlOperation.setCostTime(end - start);
        }
    }
}
