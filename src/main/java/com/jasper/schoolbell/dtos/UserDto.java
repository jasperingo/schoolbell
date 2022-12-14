package com.jasper.schoolbell.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDateTime createdAt;
}
