package com.thg.redis.model;

import java.util.List;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/17 20:17
 **/

@Data
public class RedisData {
    List<RedisBase> init;
    List<RedisBase> check;
}
