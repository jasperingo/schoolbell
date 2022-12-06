package com.jasper.schoolbell.dtos;

import com.jasper.schoolbell.entities.EventOccurrence;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventOccurrenceDto {
    private Long id;

    private String description;

    private EventOccurrence.Venue venue;

    private String address;

    private String link;

    private int duration;

    private LocalDateTime startedAt;

    private LocalDateTime cancelledAt;

    private LocalDateTime createdAt;

    @Data
    public static class WithRelations extends EventOccurrenceDto {
        private EventDto event;
    }
}
