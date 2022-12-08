package com.jasper.schoolbell.validators;

import com.jasper.schoolbell.dtos.EventOccurrenceCreateDto;
import com.jasper.schoolbell.entities.Event;
import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.repositories.EventsRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZoneOffset;

public class EventOccurrenceStartDateIsUniqueValidator implements ConstraintValidator<EventOccurrenceStartDateIsUnique, EventOccurrenceCreateDto> {
    @Inject
    private EventsRepository eventsRepository;

    @Override
    public boolean isValid(EventOccurrenceCreateDto dto, ConstraintValidatorContext constraintValidatorContext) {
        final long startEpoch = dto.getStartedAt().toEpochSecond(ZoneOffset.UTC);
        final long endEpoch = dto.getStartedAt().plusMinutes(dto.getDuration()).toEpochSecond(ZoneOffset.UTC);

        try {
            final Event event = eventsRepository.findById(dto.getEventId());

            return validate(startEpoch, endEpoch, event);
        } catch (NoResultException | IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean validate(long startEpoch, long endEpoch, Event event) {
        for (EventOccurrence eventOccurrence: event.getEventOccurrences()) {
            if (eventOccurrence.getCancelledAt() == null) {
                long se = eventOccurrence.getStartedAt().toEpochSecond(ZoneOffset.UTC);
                long ee = eventOccurrence.getStartedAt().plusMinutes(eventOccurrence.getDuration()).toEpochSecond(ZoneOffset.UTC);

                if (startEpoch == se || endEpoch == ee) {
                    throw new IllegalArgumentException();
                }
            }
        }

        return true;
    }
}
