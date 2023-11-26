package com.thg.mysql.model;

import java.util.Map;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/12 20:54
 **/

@Data
public class BaseMysqlData {
    String dsName;                  /* 数据源名 */
    String table;                   /* 库表名 */
    Map<String,Object> fields;      /* KV字段集合 */
    Boolean useModel = true;        /* 是否使用模版注入 */
}
