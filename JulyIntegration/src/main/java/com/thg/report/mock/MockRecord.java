package com.thg.report.mock;

import lombok.Data;
import org.mockito.invocation.Location;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/27 14:36
 **/


@Data
public class MockRecord {
    private Location location;
    private String method;
    private String returnValue;
    private Boolean isStubbing;


    public MockRecord(Location location, String method, String returnValue, Boolean isStubbing) {
        this.location = location;
        this.method = method;
        this.returnValue = returnValue;
        this.isStubbing = isStubbing;
    }
}
