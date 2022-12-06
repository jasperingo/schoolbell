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

    @Data
    public static class WithRelations extends EventDto {
        private List<ParticipantDto> participants;

        private List<EventOccurrenceDto> eventOccurrences;
    }
}
