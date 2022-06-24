package com.authga.springcloudaws.producer;

import com.authga.springcloudaws.model.Event;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.authga.springcloudaws.config.AWSConfigConstants.ORDER_CREATED_TOPIC;
import static com.authga.springcloudaws.config.AWSConfigConstants.ORDER_QUEUE;

@Slf4j
@Service
public class SimpleMessageProducer {

//    @Autowired
//    private NotificationMessagingTemplate notificationMessagingTemplate;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    public void sendBySNS(Event event) {
//         SNS topic模式专用 Template
//        notificationMessagingTemplate.convertAndSend(ORDER_CREATED_TOPIC, event);
    }

    public void sendBySQS(Event event) {
        // SQS 简单点对点模式专用 Template
        queueMessagingTemplate.convertAndSend(ORDER_QUEUE, event);
    }
}
