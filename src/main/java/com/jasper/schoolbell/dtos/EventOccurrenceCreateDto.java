package com.jasper.schoolbell.dtos;

import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.validators.EventIdBelongToAuthUser;
import com.jasper.schoolbell.validators.EventIdExists;
import com.jasper.schoolbell.validators.EventOccurrenceStartDateIsUnique;
import com.jasper.schoolbell.validators.EventOccurrenceVenueIsCorrect;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EventOccurrenceVenueIsCorrect(payload = { EventOccurrenceVenueIsCorrect.MyPayload.class })
@EventOccurrenceStartDateIsUnique(payload = { EventOccurrenceStartDateIsUnique.MyPayload.class })
public class EventOccurrenceCreateDto {
    @NotNull
    @Min(1)
    @EventIdExists
    @EventIdBelongToAuthUser
    private Long eventId;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private EventOccurrence.Venue venue;

    private String address;

    private String link;

    @NotNull
    @Min(1)
    private int duration;

    @NotNull
    @Future
    private LocalDateTime startedAt;
}
