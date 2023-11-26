package com.thg.report.mysql;

import com.thg.report.mysql.CheckTableResult;
import com.thg.report.mysql.SqlOperation;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/14 17:27
 **/

@Data
public class MysqlTestReport {
    List<SqlOperation> sqlOperationList = new ArrayList<>();
    List<CheckTableResult> checkTableResultList = new ArrayList<>();
}
