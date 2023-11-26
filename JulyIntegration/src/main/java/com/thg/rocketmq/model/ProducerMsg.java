package com.thg.rocketmq.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.thg.utils.JsonUtils;
import java.util.Map;
import lombok.Data;
import org.apache.rocketmq.common.message.Message;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/19 15:17
 **/

@Data
public class ProducerMsg {
    String topic;
    Map<String, Object> message;
    Map<String, String> properties;


    public static ProducerMsg convertFromMqMessage(Message message){
        ProducerMsg producerMsg = new ProducerMsg();
        producerMsg.setTopic(message.getTopic());
        producerMsg.setMessage(JsonUtils.deserialize(message.getBody(),
            new TypeReference<Map<String, Object>>(){}));
        producerMsg.setProperties(message.getProperties());
        return producerMsg;
    }

}
