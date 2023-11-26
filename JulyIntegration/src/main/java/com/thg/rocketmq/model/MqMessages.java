package com.thg.rocketmq.model;

import java.util.List;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/19 15:23
 **/

@Data
public class MqMessages {
    List<ProducerMsg> producer;
    List<ConsumerMsg> consumer;
}
