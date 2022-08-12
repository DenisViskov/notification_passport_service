package com.example.notification_passport_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NotificationPassportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationPassportServiceApplication.class, args);
    }

}
