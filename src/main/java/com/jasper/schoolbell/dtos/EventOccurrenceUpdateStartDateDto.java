package com.jasper.schoolbell.dtos;

import com.jasper.schoolbell.validators.EventOccurrenceUpdateStartDateIsUnique;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventOccurrenceUpdateStartDateDto {
    @NotNull
    @Future
    @EventOccurrenceUpdateStartDateIsUnique
    private LocalDateTime startedAt;
}
