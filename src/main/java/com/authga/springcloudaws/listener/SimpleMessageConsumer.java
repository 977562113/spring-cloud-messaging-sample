package com.authga.springcloudaws.listener;

import com.authga.springcloudaws.model.Event;
import io.awspring.cloud.messaging.config.annotation.NotificationMessage;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import static com.authga.springcloudaws.config.AWSConfigConstants.ORDER_QUEUE;


@Slf4j
@Controller
public class SimpleMessageConsumer implements MessageConsumer {

    @Override
    @SqsListener(value = ORDER_QUEUE, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void consume(@NotificationMessage Event event) {
        if (event != null) {
            log.info("Received order event for consumer 1: " + event);
        }
    }
}
