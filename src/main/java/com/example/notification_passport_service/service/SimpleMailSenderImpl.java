package com.example.notification_passport_service.service;

import com.example.notification_passport_service.dto.ExpiredPassportDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SimpleMailSenderImpl implements Sender<ExpiredPassportDto> {

    @SneakyThrows
    @Override public void sendMessage(final ExpiredPassportDto info) {
        log.info("try to send message on issuing_authority@notify.com");
        TimeUnit.SECONDS.sleep(2L);
        log.info("message with body: {} was sent successful", info);
    }
}
