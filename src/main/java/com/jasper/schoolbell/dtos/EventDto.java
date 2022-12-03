package com.jasper.schoolbell.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventDto {
    private Long id;

    private String title;

    private String description;

    private LocalDateTime createdAt;

    private List<ParticipantDto> participants;
}
