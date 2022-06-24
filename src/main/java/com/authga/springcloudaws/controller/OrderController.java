package com.authga.springcloudaws.controller;

import com.authga.springcloudaws.model.Event;
import com.authga.springcloudaws.model.EventData;
import com.authga.springcloudaws.model.EventType;
import com.authga.springcloudaws.producer.SimpleMessageProducer;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.authga.springcloudaws.config.AWSConfigConstants.ORDER_QUEUE;

@Controller
public class OrderController {

    private final AtomicInteger atomicInteger = new AtomicInteger();
    @Autowired
    private SimpleMessageProducer simpleMessageProducer;

    @GetMapping(value = "/sendBySNS", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sendBySNS() {
        Event event = createOrderEvent("topic");
        simpleMessageProducer.sendBySNS(event);
        return ResponseEntity.ok().body("Published message on SNS");
    }

    @GetMapping(value = "/sendBySQS", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sendBySQS() {
        Event event = createOrderEvent("single");
        simpleMessageProducer.sendBySQS(event);
        return ResponseEntity.ok().body("Published message on SNS");
    }

    private Event createOrderEvent(String sign) {
        return Event.builder()
                .eventId(UUID.randomUUID().toString())
                .occurredAt(Instant.now().toString())
                .version(String.valueOf(atomicInteger.getAndIncrement()))
                .data(EventData
                        .builder()
                        .eventType(EventType.ORDER_CREATED)
                        .orderId(UUID.randomUUID().toString())
                        .owner("SampleProducer" + sign)
                        .build())
                .build();
    }
}
