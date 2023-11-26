package com.thg.core.action;

import com.thg.TestContext;
import com.thg.ComponentService;
import com.thg.core.model.BaseCase;
import com.thg.report.enmu.CasePace;
import com.thg.utils.JsonUtils;
import com.thg.utils.ReportUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/21 15:52
 **/
@Slf4j
@Component
public abstract class BottomTestBase <T extends BaseCase> extends AbstractTestNGSpringContextTests {


    @Autowired
    protected List<ComponentService> componentServiceList;


    abstract protected void doAction(T t);

    void clearDataBeforeTest(T t) {
        batchExecute(ComponentService::clearDataBeforeTest,t);
    }

    void initDataSetBeforeTest(T t){
        batchExecute(ComponentService::initDataBeforeTest,t);
    }


    void checkDataAfterTest(T t){
        batchExecute(ComponentService::checkDataAfterTest,t);
    }

    void clearDataAfterTest(T t){
        batchExecute(ComponentService::clearDataAfterTest,t);
    }


    private void batchExecute(BiConsumer<ComponentService, Object> consumer, T t){
        for (ComponentService componentService : componentServiceList) {
            Object paramsValue = t.getComponentMap().get(componentService.getName());
            if (paramsValue != null) {
                consumer.accept(componentService, paramsValue);
            }
        }
    }

    protected void around(Consumer<T> consumer,T t, CasePace casePace){
        TestContext.casePace = casePace;
        long start = System.currentTimeMillis();
        consumer.accept(t);
        long end = System.currentTimeMillis();
        ReportUtils.setPaceTestTime(t.getCaseId(), casePace, end - start);
    }

    void testCaseMarkBeforeTest(T t){
        TestContext.currentCase = t.getCaseId();
        TestContext.cacheObjectMapper = JsonUtils.generaterObjectMapper(t.getRandom());
        ReportUtils.setNewJulyReportInfo(t);
        ReportUtils.setCaseStartTime(t.getCaseId(), LocalDateTime.now());
    }

    void testCaseMarkAfterTest(T t){
        ReportUtils.setCaseEndTime(t.getCaseId(), LocalDateTime.now());
    }
}
