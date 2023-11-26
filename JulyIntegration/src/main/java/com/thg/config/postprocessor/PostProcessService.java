package com.thg.config.postprocessor;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/27 16:00
 **/
public interface PostProcessService {

    /**
     * 判断service是否命中
     *
     * @param bean spring bean
     * @param beanName spring bean name
     * @return 是否命中
     */
    boolean hit(Object bean,String beanName);


    /**
     * 命中bean处理函数
     *
     * @param bean spring bean
     * @param beanName spring bean name
     * @return new bean
     */
    Object beanProcess(Object bean,String beanName);
}
