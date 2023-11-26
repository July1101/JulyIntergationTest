package com.thg.rocketmq.mock;

import com.thg.rocketmq.model.ProducerMsg;
import com.thg.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/19 15:42
 **/

@Component
public class ProducerMockService {


    @MockBean
    private DefaultMQProducer defaultMQProducer;

    private static List<Message> messages = new ArrayList<>();

    public void mockRocketMQSendMessage() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Mockito.doAnswer( invocationOnMock -> {
            Message message = invocationOnMock.getArgument(0, Message.class);
            messages.add(message);
            SendResult sendResult = new SendResult();
            sendResult.setSendStatus(SendStatus.SEND_OK);
            return buildSendResult();
        }).when(defaultMQProducer).send(Mockito.any(Message.class));
    }
    private SendResult buildSendResult(){
        SendResult sendResult = new SendResult();
        sendResult.setSendStatus(SendStatus.SEND_OK);
        sendResult.setMsgId(String.valueOf(RandomUtils.nextLong()));
        sendResult.setMessageQueue(new MessageQueue("test","testBroker", RandomUtils.nextInt()));
        return sendResult;
    }


    public List<ProducerMsg> getProducerMessages(){
        return messages.stream().map(ProducerMsg::convertFromMqMessage)
            .collect(Collectors.toList());
    }

    public void clearMock(){
        messages.clear();
        Mockito.clearInvocations(defaultMQProducer);
    }

}
