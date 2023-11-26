package com.thg.mock;

import com.thg.TestContext;
import com.thg.ComponentService;
import com.thg.mock.model.MockData;
import com.thg.mock.model.TestMock;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/14 21:38
 **/
@Slf4j
@Component
public class MockComponentServiceImpl implements ComponentService<List<MockData>> {

    private static final String MOCK = "mock";

    @Override
    public String getName() {
        return MOCK;
    }

    @Override
    public void clearDataBeforeTest(List<MockData> data) {
        this.clearMock(data);
    }

    @Override
    public void initDataBeforeTest(List<MockData> data) {
        for (MockData mockData : data) {
            String mockId = mockData.getMockId();
            TestMock testMock = TestContext.testMockMap.get(mockId);
            if (testMock != null) {
                testMock.invokeMockMethod(mockData.getMethodParams(), TestContext.cacheObjectMapper);
            } else {
                log.error("Mock in before test fail | no testMock which name:{}", mockId);
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void checkDataAfterTest(List<MockData> data) {

    }

    @Override
    public void clearDataAfterTest(List<MockData> data) {
        this.clearMock(data);
    }

    private void clearMock(List<MockData> data){
        for (MockData mockData : data) {
            String mockId = mockData.getMockId();
            TestMock testMock = TestContext.testMockMap.get(mockId);
            if (testMock != null) {
                testMock.invokeUnMockMethod();
            }else {
                log.error("UnMock fail  | no testMock which name:{}",mockId);
                throw new RuntimeException();
            }
        }
    }

}
