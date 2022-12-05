package com.jasper.schoolbell.filters;

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

@Provider
@EventJoinPermission
@Priority(Priorities.AUTHORIZATION)
public class EventJoinPermissionFilter implements ContainerRequestFilter {
    @Inject
    private RequestParamService requestParamService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        final User user = requestParamService.getAuthUser();

        requestParamService.getEvent().getParticipants()
            .stream()
            .filter(participant -> Objects.equals(participant.getUser().getId(), user.getId()))
            .findFirst()
            .ifPresent(host -> { throw new ForbiddenException("You are already a participant of this event"); });

    }
}
