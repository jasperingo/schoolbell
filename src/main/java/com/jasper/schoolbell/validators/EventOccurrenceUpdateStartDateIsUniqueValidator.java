package com.jasper.schoolbell.validators;

import com.jasper.schoolbell.entities.Event;
import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.repositories.EventsRepository;
import com.jasper.schoolbell.services.RequestParamService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class EventOccurrenceUpdateStartDateIsUniqueValidator implements ConstraintValidator<EventOccurrenceUpdateStartDateIsUnique, LocalDateTime> {
    @Inject
    private EventsRepository eventsRepository;

    @Inject
    private RequestParamService requestParamService;

    @Override
    public boolean isValid(LocalDateTime startDate, ConstraintValidatorContext constraintValidatorContext) {
        final EventOccurrence occurrence = requestParamService.getEventOccurrence();

        try {
            if (startDate == null) {
                throw new IllegalArgumentException();
            }

            final long startEpoch = startDate.toEpochSecond(ZoneOffset.UTC);
            final long endEpoch = startDate.plusMinutes(occurrence.getDuration()).toEpochSecond(ZoneOffset.UTC);

            final Event event = eventsRepository.findById(occurrence.getEvent().getId());

            return EventOccurrenceStartDateIsUniqueValidator.validate(startEpoch, endEpoch, event);
        } catch (NoResultException | IllegalArgumentException e) {
            return false;
        }
    }


}
