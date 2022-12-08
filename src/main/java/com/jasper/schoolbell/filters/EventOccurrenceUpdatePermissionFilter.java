package com.jasper.schoolbell.filters;

import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.entities.User;
import com.jasper.schoolbell.repositories.EventsRepository;
import com.jasper.schoolbell.services.RequestParamService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Provider
@EventOccurrenceUpdatePermission
@Priority(Priorities.AUTHORIZATION)
public class EventOccurrenceUpdatePermissionFilter implements ContainerRequestFilter {
    @Inject
    private RequestParamService requestParamService;

    @Inject
    private EventsRepository eventsRepository;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        final User user = requestParamService.getAuthUser();

        final EventOccurrence eventOccurrence = requestParamService.getEventOccurrence();

        if (eventOccurrence.getCancelledAt() != null) {
            throw new ForbiddenException("This event has been cancelled");
        }

        if (
            eventOccurrence.getStartedAt().plusMinutes(eventOccurrence.getDuration()).toEpochSecond(ZoneOffset.UTC) <=
            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        ) {
            throw new ForbiddenException("This event has been ended");
        }

        eventsRepository.findById(eventOccurrence.getEvent().getId())
            .getParticipants()
            .stream()
            .filter(participant -> Objects.equals(participant.getUser().getId(), user.getId()) && participant.isHost())
            .findFirst()
            .orElseThrow(() -> new ForbiddenException("You are not the host of the event"));

    }
}
