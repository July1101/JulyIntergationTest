package com.thg.rocketmq;

import com.thg.ComponentService;
import com.thg.rocketmq.mock.ProducerMockService;
import com.thg.rocketmq.model.MqMessages;
import com.thg.rocketmq.model.ProducerMsg;
import com.thg.utils.ComponentUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.testng.Assert;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/19 15:25
 **/

@Service
public class RocketMqComponentService implements ComponentService<MqMessages> {


    private final ProducerMockService producerMockService ;

    private final List<RocketMQListener> rocketMQListenerList;

    private static final Map<String,RocketMQListener> handlerServiceMap = new HashMap<>();

    public RocketMqComponentService(ProducerMockService producerMockService,
        List<RocketMQListener> rocketMQListenerList) {
        this.producerMockService = producerMockService;
        this.rocketMQListenerList = rocketMQListenerList;
    }


    @Override
    public String getName() {
        return "mq";
    }

    @Override
    public void clearDataBeforeTest(MqMessages data) {
        producerMockService.clearMock();
    }


    @Override
    public void initDataBeforeTest(MqMessages data) {
        if (CollectionUtils.isEmpty(rocketMQListenerList)) {
            return;
        }
        for (RocketMQListener rocketMQListener : rocketMQListenerList) {
            RocketMQMessageListener annotation = rocketMQListener.getClass()
                .getAnnotation(RocketMQMessageListener.class);
            handlerServiceMap.put(buildKey(annotation.consumerGroup(), annotation.topic()),
                rocketMQListener);
        }
    }

    @Override
    public void checkDataAfterTest(MqMessages data) {
        List<ProducerMsg> actualMessages = producerMockService.getProducerMessages();
        if (data == null || data.getProducer() == null || data.getProducer().size() == 0) {
            Assert.assertTrue(CollectionUtils.isEmpty(actualMessages), "Expect mq is null,"
                + ",but the actual mq size is " + actualMessages.size());
            return ;
        }
        List<ProducerMsg> expectMessages = data.getProducer();
        Assert.assertEquals(actualMessages.size(), expectMessages.size(),
            "The mq messages size is not matches , "
                + "expect is " + expectMessages.size() + " but actual is " + actualMessages.size());
        checkMqFields(actualMessages,expectMessages);
    }

    @Override
    public void clearDataAfterTest(MqMessages data) {
        producerMockService.clearMock();
    }

    void checkMqFields(List<ProducerMsg> actualMessages, List<ProducerMsg> expectMessages) {
        List<ProducerMsg> copyActualMessages = new ArrayList<>(actualMessages);
        for (ProducerMsg expectMessage : expectMessages) {
            boolean flag = false;
            for (ProducerMsg actualMessage : copyActualMessages) {
                if (matchMessage(expectMessage, actualMessage)) {
                    copyActualMessages.remove(actualMessage);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                Assert.fail(
                    "There has a message do not matches actual, which expect is " + expectMessage);
            }
        }
    }

    boolean matchMessage(ProducerMsg expect, ProducerMsg actual) {
        if (!actual.getTopic().equals(expect.getTopic())) {
            return false;
        }
        return ComponentUtils.compareMap(expect.getMessage(), actual.getMessage()) &&
            ComponentUtils.compareMap(expect.getProperties(), actual.getProperties());
    }

    String buildKey(String group, String topic) {
        return group + "-" + topic;
    }


}
