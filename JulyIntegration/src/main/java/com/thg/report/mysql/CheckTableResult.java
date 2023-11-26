package com.thg.report.mysql;

import java.util.Map;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/14 17:45
 **/
@Data
public class CheckTableResult {
    String dsName;
    String tableName;
    Map<String,Object> expectMap;
    Map<String,Object> actualMap;
}
