package com.jasper.schoolbell.resources;

import com.jasper.schoolbell.dtos.EventOccurrenceCreateDto;
import com.jasper.schoolbell.dtos.EventOccurrenceDto;
import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.filters.HttpStatus;
import com.jasper.schoolbell.filters.JwtAuth;
import com.jasper.schoolbell.filters.ResponseMapper;
import com.jasper.schoolbell.repositories.EventOccurrencesRepository;
import com.jasper.schoolbell.repositories.EventsRepository;
import com.jasper.schoolbell.services.ModelMapperService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@JwtAuth
@Path("event-occurrences")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ResponseMapper(EventOccurrenceDto.WithRelations.class)
public class EventOccurrencesResource {
    @Inject
    private ModelMapperService modelMapperService;

    @Inject
    private EventsRepository eventsRepository;

    @Inject
    private EventOccurrencesRepository eventOccurrencesRepository;

    @POST
    @HttpStatus(Response.Status.CREATED)
    public EventOccurrence create(@NotNull @Valid final EventOccurrenceCreateDto eventOccurrenceDto) {
        final EventOccurrence eventOccurrence = modelMapperService.map(eventOccurrenceDto, EventOccurrence.class);

        eventOccurrence.setEvent(eventsRepository.findById(eventOccurrenceDto.getEventId()));

        eventOccurrencesRepository.save(eventOccurrence);

        return eventOccurrence;
    }
}
