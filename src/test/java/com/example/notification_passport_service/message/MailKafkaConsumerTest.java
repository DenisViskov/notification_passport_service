package com.example.notification_passport_service.message;

import com.example.notification_passport_service.config.TestProducerConfig;
import com.example.notification_passport_service.dto.ExpiredPassportDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092",
        "port=9092"
    }
)
@Import(TestProducerConfig.class)
class MailKafkaConsumerTest {

    @Autowired
    private KafkaTemplate<Long, ExpiredPassportDto> producer;

    @Value("${spring.kafka.topic}")
    private String topic;

    private final ExpiredPassportDto stub = ExpiredPassportDto.builder()
        .id(1L)
        .serial(1234L)
        .number(123456L)
        .build();

    @Test
    void acceptMessage() {
        final var resultListenableFuture = producer.send(topic, stub);
        System.out.println();
    }
}