package com.example.notification_passport_service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ExpiredPassportDto {

    private Long id;

    private Long serial;

    private Long number;

    private Date expiredDate;
}
