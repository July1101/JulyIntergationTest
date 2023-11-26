package com.thg.redis.model;

import com.thg.report.enmu.CasePace;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/4 13:58
 **/

@Data
public class RedisReport {
    CasePace casePace;
    RedisDataType redisDataType;
    String methodName;
    String args;
    String returnVal;
    long timeCost;

    public RedisReport(CasePace casePace,RedisDataType type, String methodName, String args, String returnVal,
        long timeCost) {
        this.casePace = casePace;
        this.redisDataType = type;
        this.methodName = methodName;
        this.args = args;
        this.returnVal = returnVal;
        this.timeCost = timeCost;
    }
}
