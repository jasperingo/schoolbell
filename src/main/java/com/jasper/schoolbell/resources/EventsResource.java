package com.jasper.schoolbell.resources;

import com.jasper.schoolbell.dtos.EventCreateDto;
import com.jasper.schoolbell.dtos.EventDto;
import com.jasper.schoolbell.entities.Event;
import com.jasper.schoolbell.entities.Participant;
import com.jasper.schoolbell.filters.HttpStatus;
import com.jasper.schoolbell.filters.JwtAuth;
import com.jasper.schoolbell.filters.ResponseMapper;
import com.jasper.schoolbell.repositories.EventsRepository;
import com.jasper.schoolbell.repositories.ParticipantsRepository;
import com.jasper.schoolbell.services.ModelMapperService;
import com.jasper.schoolbell.services.RequestParamService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@JwtAuth
@Path("events")
@ResponseMapper(EventDto.class)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventsResource {
    @Inject
    private ModelMapperService modelMapperService;

    @Inject
    private EventsRepository eventsRepository;

    @Inject
    private ParticipantsRepository participantsRepository;

    @Inject
    private RequestParamService requestParamService;

    @POST
    @HttpStatus(Response.Status.CREATED)
    public Event create(@NotNull @Valid final EventCreateDto eventDto) {
        final Event event = modelMapperService.map(eventDto, Event.class);

        eventsRepository.save(event);

        final Participant participant = new Participant();

        participant.setHost(true);
        participant.setEvent(event);
        participant.setUser(requestParamService.getAuthUser());

        participantsRepository.save(participant);

        event.setParticipants(new ArrayList<>());
        event.getParticipants().add(participant);

        return event;
    }

    @GET
    @Path("{id}")
    public Event getOne(@PathParam("id") final Long id) {
        final Event event = eventsRepository.findById(id);

        if (event == null) {
            throw new NotFoundException("Event not found");
        }

        return event;
    }
}
