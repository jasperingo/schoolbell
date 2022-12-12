package com.jasper.schoolbell.resources;

import com.jasper.schoolbell.dtos.EventOccurrenceCreateDto;
import com.jasper.schoolbell.dtos.EventOccurrenceDto;
import com.jasper.schoolbell.dtos.EventOccurrenceUpdateStartDateDto;
import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.filters.*;
import com.jasper.schoolbell.repositories.EventOccurrencesRepository;
import com.jasper.schoolbell.repositories.EventsRepository;
import com.jasper.schoolbell.services.CallService;
import com.jasper.schoolbell.services.ModelMapperService;
import com.jasper.schoolbell.services.RequestParamService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@JwtAuth
@Path("event-occurrences")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ResponseMapper(EventOccurrenceDto.WithRelations.class)
public class EventOccurrencesResource {
    @Inject
    private CallService callService;

    @Inject
    private ModelMapperService modelMapperService;

    @Inject
    private RequestParamService requestParamService;

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

    @GET
    @Path("{id}")
    @EventOccurrenceExists
    public EventOccurrence findOne() {
        return requestParamService.getEventOccurrence();
    }

    @PUT
    @EventOccurrenceExists
    @Path("{id}/cancelled-at")
    @EventOccurrenceUpdatePermission
    public EventOccurrence updateCancelledAt() {
        final EventOccurrence eventOccurrence = requestParamService.getEventOccurrence();

        eventOccurrence.setCancelledAt(LocalDateTime.now());

        eventOccurrencesRepository.update(eventOccurrence);

        final String message = String.format(
          "your event %s scheduled for %s, has been cancelled.",
          eventOccurrence.getEvent().getTitle(),
          eventOccurrence.getStartedAt()
        );
        
        callService.sendCall(
            eventOccurrence,
            eventsRepository.findById(eventOccurrence.getEvent().getId()),
            String.format(
                "%s I repeat, %s",
                message,
                eventOccurrence.getEvent().getTitle(),
                eventOccurrence.getStartedAt(),
                message
            )
        );

        return eventOccurrence;
    }

    @PUT
    @EventOccurrenceExists
    @Path("{id}/started-at")
    @EventOccurrenceUpdatePermission
    public EventOccurrence updateStartAt(@NotNull @Valid final EventOccurrenceUpdateStartDateDto startDateDto) {
        final EventOccurrence eventOccurrence = requestParamService.getEventOccurrence();

        final LocalDateTime oldStartAt = eventOccurrence.getStartedAt();

        eventOccurrence.setStartedAt(startDateDto.getStartedAt());

        eventOccurrencesRepository.update(eventOccurrence);
        
        final String message = String.format(
            "your event %s scheduled for %s, has been postponed to %s.",
            eventOccurrence.getEvent().getTitle(),
            oldStartAt,
            eventOccurrence.getStartedAt()
        );

        callService.sendCall(
            eventOccurrence,
            eventsRepository.findById(eventOccurrence.getEvent().getId()),
            String.format(
                "%s I repeat, %s",
                message,
                eventOccurrence.getEvent().getTitle(),
                oldStartAt,
                eventOccurrence.getStartedAt(),
                message
            )
        );

        return eventOccurrence;
    }

    @POST
    @EventOccurrenceExists
    @Path("{id}/remind")
    @EventOccurrenceUpdatePermission
    public EventOccurrence remind() {
        final EventOccurrence eventOccurrence = requestParamService.getEventOccurrence();

        final String message = String.format(
           "please remember your event %s will start by %s.",
            eventOccurrence.getEvent().getTitle(),
            eventOccurrence.getStartedAt()
        );
        
        callService.sendCall(
            eventOccurrence,
            eventsRepository.findById(eventOccurrence.getEvent().getId()),
            String.format(
                "%s I repeat, %s",
                message,
                eventOccurrence.getEvent().getTitle(),
                eventOccurrence.getStartedAt(),
                message
            )
        );

        return eventOccurrence;
    }
}
