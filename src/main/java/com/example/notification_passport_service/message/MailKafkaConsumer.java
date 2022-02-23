package com.example.notification_passport_service.message;

import com.example.notification_passport_service.dto.ExpiredPassportDto;
import com.example.notification_passport_service.service.Sender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class MailKafkaConsumer {

    @Autowired
    private Sender<ExpiredPassportDto> sender;

    @KafkaListener(topics = "${spring.kafka.topic}")
    public void acceptMessage(final ConsumerRecord<Long, ExpiredPassportDto> message) {
        log.info("got message: {}", message);
        Optional.ofNullable(message.value())
            .ifPresentOrElse(
                msg -> sender.sendMessage(msg),
                () -> log.info("received message is empty")
            );
    }
}
