package com.example.notification_passport_service.message;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.notification_passport_service.config.TestProducerConfig;
import com.example.notification_passport_service.dto.ExpiredPassportDto;
import com.example.notification_passport_service.service.SimpleMailSenderImpl;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    topics = "${spring.kafka.topic}",
    brokerProperties = {
        "listeners=PLAINTEXT://${spring.kafka.bootstrap-servers}",
        "port=${spring.kafka.port}"
    }
)
@TestPropertySource(locations = "classpath:application.yaml")
@Import(TestProducerConfig.class)
class MailKafkaConsumerTest implements WithAssertions {

    @Autowired
    private KafkaTemplate<Long, ExpiredPassportDto> producer;

    @Value("${spring.kafka.topic}")
    private String topic;

    private final ExpiredPassportDto stub = ExpiredPassportDto.builder()
        .id(1L)
        .serial(1234L)
        .number(123456L)
        .build();

    private ListAppender<ILoggingEvent> logWatcher;

    @BeforeAll
    static void beforeAll() throws InterruptedException {
        giveTime();
    }

    @BeforeEach
    void setUp() {
        logWatcher = new ListAppender<>();
        logWatcher.start();
        final Logger logger = (Logger) LoggerFactory.getLogger(SimpleMailSenderImpl.class);
        logger.addAppender(logWatcher);
    }

    @Test
    void acceptMessage() throws InterruptedException {
        final var firstExpectedMessage = "try to send message on issuing_authority@notify.com";
        final var secondExpectedMessage = "message with body: "
            .concat("ExpiredPassportDto(id=1, serial=1234, number=123456, expiredDate=null) ")
            .concat("was sent successful");

        final var resultListenableFuture = producer.send(topic, stub);
        giveTime();
        final var logs = logWatcher.list;

        assertThat(resultListenableFuture.isDone()).isTrue();
        assertThat(logs).isNotNull().hasSize(2);
        assertThat(logs).allMatch(event -> event.getLevel() == Level.INFO);
        assertThat(logs).map(ILoggingEvent::getFormattedMessage)
            .contains(firstExpectedMessage, secondExpectedMessage);
    }

    private static void giveTime() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
    }
}