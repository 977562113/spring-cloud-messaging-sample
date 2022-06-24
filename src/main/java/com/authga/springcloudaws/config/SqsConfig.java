package com.authga.springcloudaws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.support.NotificationMessageArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;

import java.util.ArrayList;

import static com.authga.springcloudaws.config.AWSConfigConstants.*;

@Configuration
public class SqsConfig {

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSqs) {
        return new QueueMessagingTemplate(amazonSqs);
    }

    @Bean
    public AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(ENDPOINT, EU_CENTRAL_1);
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync(final AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return AmazonSQSAsyncClientBuilder
                                    .standard()
                                    .withEndpointConfiguration(endpointConfiguration)
                                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                                    .build();
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(MessageConverter messageConverter) {
        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        HandlerMethodArgumentResolver notificationMessageArgumentResolver = new NotificationMessageArgumentResolver(messageConverter);
        HandlerMethodArgumentResolver payloadMethodArgumentResolver = new PayloadMethodArgumentResolver(messageConverter);
        factory.setArgumentResolvers(new ArrayList<HandlerMethodArgumentResolver>(){{
            add(payloadMethodArgumentResolver);        // SQS
//            add(notificationMessageArgumentResolver);  // SNS
            //冲突: 两者只能走其中一个, 走其中一个时, 另外一种报错!
        }});
        return factory;
    }

    @Bean
    protected MessageConverter messageConverter(ObjectMapper objectMapper) {
        objectMapper.registerModules(new JavaTimeModule());

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setSerializedPayloadClass(String.class);
        converter.setStrictContentTypeMatch(false);

        return converter;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
