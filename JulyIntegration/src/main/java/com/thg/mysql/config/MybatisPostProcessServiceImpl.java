package com.thg.mysql.config;

import com.thg.config.postprocessor.PostProcessService;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/27 16:05
 **/
public class MybatisPostProcessServiceImpl implements PostProcessService {

    @Override
    public boolean hit(Object bean, String beanName) {
        return bean instanceof DefaultSqlSessionFactory;
    }

    @Override
    public Object beanProcess(Object bean, String beanName) {
        DefaultSqlSessionFactory factory = (DefaultSqlSessionFactory) bean;
        Configuration configuration = factory.getConfiguration();
        configuration.addInterceptor(new MyBatisInterceptor());
        configuration.setLogImpl(MyLog.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }
}
