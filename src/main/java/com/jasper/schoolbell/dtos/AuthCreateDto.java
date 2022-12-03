package com.jasper.schoolbell.dtos;

import lombok.Data;

@Data
public class AuthCreateDto {
    private String phoneNumber;

    private String password;
}
