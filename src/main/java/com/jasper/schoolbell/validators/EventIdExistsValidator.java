package com.jasper.schoolbell.validators;

import com.jasper.schoolbell.repositories.EventsRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventIdExistsValidator implements ConstraintValidator<EventIdExists, Long> {
    @Inject
    private EventsRepository eventsRepository;

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        try {
            eventsRepository.findById(aLong);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}
