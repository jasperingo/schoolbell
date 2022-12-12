package com.jasper.schoolbell.filters;

import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.repositories.EventOccurrencesRepository;
import com.jasper.schoolbell.repositories.EventsRepository;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(500)
@EventOccurrenceExists
public class EventOccurrenceExistsFilter implements ContainerRequestFilter {
    @Inject
    private EventsRepository eventsRepository;
  
    @Inject
    private EventOccurrencesRepository eventOccurrencesRepository;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        final String id = containerRequestContext.getUriInfo().getPathParameters().getFirst("id");

        try {
            final EventOccurrence eventOccurrence = eventOccurrencesRepository.findById(Long.parseLong(id));
            
            eventOccurrence.getEvent().setParticipants(eventsRepository.findHostParticipant(eventOccurrence.getEvent()));

            containerRequestContext.setProperty(EventOccurrence.class.getName(), eventOccurrence);
        } catch (NoResultException e) {
            throw new NotFoundException("Event occurrence not found");
        }
    }
}
