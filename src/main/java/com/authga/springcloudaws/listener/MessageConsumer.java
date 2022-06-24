package com.authga.springcloudaws.listener;

import com.authga.springcloudaws.model.Event;
import io.awspring.cloud.messaging.config.annotation.NotificationMessage;

public interface MessageConsumer {

    void consume(@NotificationMessage Event event);
}
