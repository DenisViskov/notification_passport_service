package com.example.notification_passport_service.config;

import com.example.notification_passport_service.dto.ExpiredPassportDto;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
@AllArgsConstructor
public class KafkaConsumerConfig {

    @Autowired
    private final KafkaConfigurationProperties kafkaConfigurationProperties;

    @Bean
    public KafkaListenerContainerFactory<?> kafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<Long, ExpiredPassportDto> factory
            = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<Long, ExpiredPassportDto> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    private Map<String, Object> consumerConfigs() {
        return Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigurationProperties.getBootstrapServers(),
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            ConsumerConfig.GROUP_ID_CONFIG, kafkaConfigurationProperties.getConsumer().getGroupId(),
            JsonDeserializer.TYPE_MAPPINGS, kafkaConfigurationProperties.getTypeMapping(),
            JsonDeserializer.TRUSTED_PACKAGES, "*"
        );
    }
}
