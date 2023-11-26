package com.thg.report.mysql;

import com.thg.report.enmu.CasePace;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/14 17:32
 **/
@Data
public class SqlOperation {
    CasePace pace;
    String sql;
    String res;
    Long costTime;
}
