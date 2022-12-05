package com.jasper.schoolbell.filters;

import com.jasper.schoolbell.entities.Event;
import com.jasper.schoolbell.repositories.EventsRepository;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@EventExists
@Priority(500)
public class EventExistsFilter implements ContainerRequestFilter {
    @Inject
    private EventsRepository eventsRepository;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        final String id = containerRequestContext.getUriInfo().getPathParameters().getFirst("id");

        try {
            final Event event = eventsRepository.findById(Long.parseLong(id));

            containerRequestContext.setProperty(Event.class.getName(), event);
        } catch (NoResultException e) {
            throw new NotFoundException("Event not found");
        }
    }
}
