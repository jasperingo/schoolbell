package com.jasper.schoolbell.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    Long id;

    String firstName;

    String lastName;

    String phoneNumber;

    LocalDateTime createdAt;
}
