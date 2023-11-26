package com.thg.mysql.model;

import java.util.List;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/18 15:32
 **/

@Data
public class MysqlData {
    List<InitMysqlData> initDataSet;
    List<CheckMysqlData> checkDataSet;
}
