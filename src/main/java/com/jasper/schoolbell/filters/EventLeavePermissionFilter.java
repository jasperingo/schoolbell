package com.jasper.schoolbell.filters;

import com.jasper.schoolbell.entities.Participant;
import com.jasper.schoolbell.entities.User;
import com.jasper.schoolbell.services.RequestParamService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.util.Objects;
import java.util.Optional;

@Provider
@EventLeavePermission
@Priority(Priorities.AUTHORIZATION)
public class EventLeavePermissionFilter implements ContainerRequestFilter {
    @Inject
    private RequestParamService requestParamService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        final User user = requestParamService.getAuthUser();

        final Optional<Participant> participantOptional = requestParamService.getEvent().getParticipants()
            .stream()
            .filter(participant -> Objects.equals(participant.getUser().getId(), user.getId()))
            .findFirst();

        if (participantOptional.isPresent() && participantOptional.get().isHost()) {
            throw new ForbiddenException("Host cannot leave the event");
        } else if (!participantOptional.isPresent()) {
            throw new ForbiddenException("You are not a participant of this event");
        } else {
            containerRequestContext.setProperty(Participant.class.getName(), participantOptional.get());
        }
    }
}
