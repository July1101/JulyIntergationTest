package com.thg.redis.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/17 20:10
 **/

@Data
public class RedisBase {
    String clazz;
    RedisDataType type;
    String jsName;
    String key;
    Object value;
    List<Object> list;
    Map<String,Object> hash;
    List<Object> set;
    Set<TypedTuple<Object>> zset;
}
