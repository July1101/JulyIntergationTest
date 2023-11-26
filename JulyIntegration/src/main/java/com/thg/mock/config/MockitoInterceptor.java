package com.thg.mock.config;

import com.alibaba.fastjson.JSON;
import com.thg.report.mock.MockRecord;
import com.thg.utils.ReportUtils;
import org.mockito.invocation.Location;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/27 16:50
 **/
public class MockitoInterceptor implements InvocationListener {

    @Override
    public void reportInvocation(MethodInvocationReport report) {
        Location location = report.getInvocation().getLocation();
        String method = report.getInvocation().toString();
        Boolean isStubbing = report.getLocationOfStubbing() != null;
        String returnValue;
        if (report.threwException()) {
            returnValue = report.getThrowable().toString();
        } else {
            returnValue = JSON.toJSONString(report.getReturnedValue());
        }
        MockRecord mockRecord = new MockRecord(location, method, returnValue, isStubbing);
        ReportUtils.addMockRecord(mockRecord);
    }
}
