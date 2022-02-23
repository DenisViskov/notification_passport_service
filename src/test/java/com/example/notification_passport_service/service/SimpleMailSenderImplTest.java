package com.example.notification_passport_service.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.notification_passport_service.dto.ExpiredPassportDto;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimpleMailSenderImplTest implements WithAssertions {

    @Autowired
    private Sender<ExpiredPassportDto> sender;

    private ListAppender<ILoggingEvent> logWatcher;

    private final ExpiredPassportDto stub = ExpiredPassportDto.builder()
        .id(1L)
        .serial(1234L)
        .number(123456L)
        .build();

    @BeforeEach
    void setUp() {
        logWatcher = new ListAppender<>();
        logWatcher.start();
        final Logger logger = (Logger) LoggerFactory.getLogger(SimpleMailSenderImpl.class);
        logger.addAppender(logWatcher);
    }

    @Test
    void sendMessage() {
        final var firstExpectedMessage = "try to send message on issuing_authority@notify.com";
        final var secondExpectedMessage = "message with body: "
            .concat("ExpiredPassportDto(id=1, serial=1234, number=123456, expiredDate=null) ")
            .concat("was sent successful");

        sender.sendMessage(stub);
        final var eventList = logWatcher.list;

        assertThat(eventList).isNotNull().hasSize(2);
        assertThat(eventList).allMatch(event -> event.getLevel() == Level.INFO);
        assertThat(eventList).map(ILoggingEvent::getFormattedMessage)
            .contains(firstExpectedMessage, secondExpectedMessage);
    }
}