package com.jasper.schoolbell.validators;

import com.jasper.schoolbell.dtos.EventOccurrenceCreateDto;
import com.jasper.schoolbell.entities.EventOccurrence;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class EventOccurrenceVenueIsCorrectValidator implements ConstraintValidator<EventOccurrenceVenueIsCorrect, EventOccurrenceCreateDto> {
    @Override
    public boolean isValid(EventOccurrenceCreateDto eventOccurrenceCreateDto, ConstraintValidatorContext constraintValidatorContext) {
        if (
            eventOccurrenceCreateDto.getVenue() == EventOccurrence.Venue.virtual &&
            eventOccurrenceCreateDto.getLink() != null
        ) {
            try {
                new URL(eventOccurrenceCreateDto.getLink()).toURI();
                return true;
            } catch (MalformedURLException | URISyntaxException e) {
                return false;
            }
        }

        return (
            eventOccurrenceCreateDto.getVenue() == EventOccurrence.Venue.physical &&
            eventOccurrenceCreateDto.getAddress() != null &&
            eventOccurrenceCreateDto.getAddress().length() > 0
        );
    }
}
