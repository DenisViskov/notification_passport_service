package com.example.notification_passport_service.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties(value = "spring.kafka")
public class KafkaConfigurationProperties {

    private final Consumer consumer;

    private final String bootstrapServers;

    private final String topic;

    private final String typeMapping;

    @Getter
    @ConstructorBinding
    @AllArgsConstructor
    static class Consumer {
        private final String groupId;
        private final String autoOffsetReset;
    }
}
