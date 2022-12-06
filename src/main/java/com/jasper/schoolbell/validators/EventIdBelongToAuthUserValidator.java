package com.jasper.schoolbell.validators;

import com.jasper.schoolbell.entities.User;
import com.jasper.schoolbell.repositories.EventsRepository;
import com.jasper.schoolbell.services.RequestParamService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class EventIdBelongToAuthUserValidator implements ConstraintValidator<EventIdBelongToAuthUser, Long> {
    @Inject
    private EventsRepository eventsRepository;

    @Inject
    private RequestParamService requestParamService;

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        final User user = requestParamService.getAuthUser();

        try {
            eventsRepository.findById(aLong).getParticipants()
                    .stream()
                    .filter(participant -> Objects.equals(participant.getUser().getId(), user.getId()) && participant.isHost())
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);

            return true;
        } catch (NoResultException | IllegalArgumentException e) {
            return false;
        }
    }
}
