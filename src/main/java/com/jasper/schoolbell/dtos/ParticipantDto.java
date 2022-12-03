package com.jasper.schoolbell.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipantDto {
    private Long id;

    private boolean host;

    private LocalDateTime createdAt;

    private UserDto user;
}
