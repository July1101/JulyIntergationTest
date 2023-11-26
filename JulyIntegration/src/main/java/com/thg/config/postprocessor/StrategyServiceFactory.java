package com.thg.config.postprocessor;

import com.thg.mock.config.MockMethodPostProcessServiceImpl;
import com.thg.mysql.config.MybatisPostProcessServiceImpl;
import com.thg.redis.config.RedisTemplatePostProcessServiceImpl;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/27 16:19
 **/
public class StrategyServiceFactory {

    private final List<PostProcessService> processServiceList;

    public StrategyServiceFactory() {
        this.processServiceList = new ArrayList<>();
        this.processServiceList.add(new MybatisPostProcessServiceImpl());
        this.processServiceList.add(new MockMethodPostProcessServiceImpl());
        this.processServiceList.add(new RedisTemplatePostProcessServiceImpl());
    }

    @Nullable
    public PostProcessService selectService(Object bean, String beanName) {
        PostProcessService result = null;
        for (PostProcessService postProcessService : processServiceList) {
            if (postProcessService.hit(bean, beanName)) {
                if (result == null) {
                    result = postProcessService;
                } else {
                    throw new RuntimeException("duplicate strategy when select" + beanName);
                }
            }
        }
        return result;
    }
}
