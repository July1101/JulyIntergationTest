package com.thg.report;

import com.thg.redis.model.RedisReport;
import com.thg.core.model.BaseCase;
import com.thg.report.enmu.CasePace;
import com.thg.report.mock.MockRecord;
import com.thg.report.mysql.MysqlTestReport;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/14 18:00
 **/

@Data
public class JulyReportInfo {
    private Long caseId;
    private String caseName;
    private String className;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String methodName;
    private String description;
    private String status;
    private Long totalCostTime;
    MysqlTestReport mysqlTestReport = new MysqlTestReport();
    Map<CasePace,Long> costTimeDetail = new HashMap<>();
    List<MockRecord> mockRecords = new ArrayList<>();
    List<RedisReport> redisReports = new ArrayList<>();

    public JulyReportInfo(BaseCase baseCase) {
        this.caseId = baseCase.getCaseId();
        this.description = baseCase.getName();
    }
}
