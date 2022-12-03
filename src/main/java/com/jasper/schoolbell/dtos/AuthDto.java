package com.jasper.schoolbell.dtos;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class AuthDto {
    String accessToken;

    LocalDateTime expirationDate;
}
