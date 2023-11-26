package com.thg.utils;

import com.thg.TestContext;
import com.thg.redis.model.RedisReport;
import com.thg.core.model.BaseCase;
import com.thg.report.mock.MockRecord;
import com.thg.report.JulyReportInfo;
import com.thg.report.enmu.CasePace;
import com.thg.report.mysql.MysqlTestReport;
import com.thg.report.mysql.SqlOperation;
import java.time.LocalDateTime;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/23 21:50
 **/
public class ReportUtils {


    public static void setPaceTestTime(Long caseId, CasePace casePace,long spendTime){
        JulyReportInfo reportInfo = getCaseReportById(caseId);
        reportInfo.getCostTimeDetail().put(casePace,spendTime);
    }


    public static void setCaseStartTime(long caseId,LocalDateTime time){
        getCaseReportById(caseId).setStartTime(time);
    }

    public static void setCaseEndTime(long caseId,LocalDateTime time){
        getCaseReportById(caseId).setEndTime(time);
    }

    public static void setTotalCostTime(long caseId,long totalTime){
        getCaseReportById(caseId).setTotalCostTime(totalTime);
    }

    public static void setNewJulyReportInfo(BaseCase t){
        JulyReportInfo reportInfo = new JulyReportInfo(t);
        TestContext.reportInfoMap.put(t.getCaseId(),reportInfo);
    }

    private static JulyReportInfo getCaseReportById(Long caseId){
        return TestContext.reportInfoMap.get(caseId);
    }

    public static SqlOperation registerSql(String sql){
        Long caseId = TestContext.currentCase;
        MysqlTestReport mysqlTestReport = getCaseReportById(caseId).getMysqlTestReport();
        SqlOperation sqlOperation = new SqlOperation();
        sqlOperation.setSql(sql);
        sqlOperation.setPace(TestContext.casePace);
        mysqlTestReport.getSqlOperationList().add(sqlOperation);
        return sqlOperation;
    }

    public static void addMockRecord(MockRecord record){
        if(TestContext.casePace == CasePace.ACTION){
            getCaseReportById(TestContext.currentCase).getMockRecords().add(record);
        }
    }

    public static void addRedisReport(RedisReport report) {
        TestContext.reportInfoMap.get(TestContext.currentCase).getRedisReports().add(report);
    }


}
