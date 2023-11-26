package com.thg.mysql.model;

import java.util.Map;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/18 15:28
 **/

@Data
public class InitMysqlData extends BaseMysqlData{
    Map<String,Object> deleteKey;
}
