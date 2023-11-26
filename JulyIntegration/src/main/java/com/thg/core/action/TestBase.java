package com.thg.core.action;

import com.thg.TestContext;
import com.thg.config.postprocessor.StrategyServiceFactory;
import com.thg.core.model.BaseCase;
import com.thg.deserialize.DeserializeService;
import com.thg.report.JulyReportInfo;
import com.thg.report.enmu.CasePace;
import com.thg.utils.GenericClassUtils;
import com.thg.utils.ReportUtils;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/24 18:34
 **/
@Component
@Slf4j
public abstract class TestBase<T extends BaseCase> extends BottomTestBase<T>{

    protected abstract String filePath();


    @Autowired
    private DeserializeService deserializeService;


    @DataProvider(name = "testData", indices = {})
    public Iterator<Object[]> provideData(Method method){
        List<T> cases = deserializeService.deserializeFromFile(filePath(),
            (Class<T>) GenericClassUtils.getTypeOfSuperClass(this.getClass(),0));
        return cases.stream().map(oneCase->new Object[]{oneCase.getName(),oneCase})
            .collect(Collectors.toList()).iterator();
    }

    protected void execute(T t){
        try {
            testCaseMarkBeforeTest(t);
            around(this::clearDataBeforeTest,t,CasePace.PRE_CLEAR);
            around(this::initDataSetBeforeTest,t,CasePace.INIT);
            around(this::doAction,t,CasePace.ACTION);
            around(this::checkDataAfterTest,t,CasePace.CHECK);
        } finally {
            around(this::clearDataAfterTest,t,CasePace.AFTER_CLEAR);
            testCaseMarkAfterTest(t);
            printTestResult(t);
        }
    }

    @Test(dataProvider = "testData")
    public void test(String caseId,T oneCase){
        log.info("the case of {} test start",caseId);
        execute(oneCase);
    }

    void printTestResult(T t){
        Long caseId = t.getCaseId();
        System.out.println("caseId = " + caseId);
        TestContext.testMockMap.forEach((k,v)->{
            System.out.println("k = " + k);
            System.out.println("v = " + v);
        });

        Map<Long, JulyReportInfo> reportInfoMap = TestContext.reportInfoMap;
        JulyReportInfo reportInfo = TestContext.reportInfoMap.get(caseId);
        System.out.println("reportInfo = " + reportInfo);
        System.out.println("reportInfo = " + reportInfo);
    }


}
