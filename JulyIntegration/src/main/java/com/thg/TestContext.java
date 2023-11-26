package com.thg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.mock.model.TestMock;
import com.thg.report.JulyReportInfo;
import com.thg.report.enmu.CasePace;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/21 19:45
 **/

@Data
public class TestContext {

    /** testMockMap, the key is mockId **/
    public static Map<String, TestMock> testMockMap = new HashMap<>();

    /** reportInfoMap ,key is caseId **/
    public static Map<Long, JulyReportInfo> reportInfoMap = new HashMap<>();

    /** mark case in running **/
    public static Long currentCase;

    /** mark case pace in running **/
    public static CasePace casePace ;

    public static Map<String,Map<String,Object>> mockBeanMap = new HashMap<>();

    public static ObjectMapper cacheObjectMapper;

}
