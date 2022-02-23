package com.example.notification_passport_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpiredPassportDto {

    private Long id;

    private Long serial;

    private Long number;

    private Date expiredDate;
}
