package com.thg.rocketmq.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/19 15:17
 **/

@Data
public class ConsumerMsg {
    String topic;
    String group;
    List<Map<String,Object>> messages;
}
