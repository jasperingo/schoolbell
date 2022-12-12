package com.jasper.schoolbell.resources;

import com.jasper.schoolbell.dtos.EventCreateDto;
import com.jasper.schoolbell.dtos.EventDto;
import com.jasper.schoolbell.entities.Event;
import com.jasper.schoolbell.entities.Participant;
import com.jasper.schoolbell.entities.User;
import com.jasper.schoolbell.filters.*;
import com.jasper.schoolbell.repositories.EventsRepository;
import com.jasper.schoolbell.repositories.ParticipantsRepository;
import com.jasper.schoolbell.services.ModelMapperService;
import com.jasper.schoolbell.services.RequestParamService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JwtAuth
@Path("events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ResponseMapper(EventDto.WithRelations.class)
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
    public List<Event> getMany() {
        return eventsRepository.findMany().stream()
            .peek(event -> event.setParticipants(eventsRepository.findHostParticipant(event)))
            .peek(event -> event.setEventOccurrences(eventsRepository.findNextOrLastEventOccurrence(event)))
            .collect(Collectors.toList());
    }

    @GET
    @EventExists
    @Path("{id}")
    public Event getOne() {
        return requestParamService.getEvent();
    }

    @POST
    @EventExists
    @Path("{id}/join")
    @EventJoinPermission
    public Event join() {
        final Event event = requestParamService.getEvent();
        final User user = requestParamService.getAuthUser();
        Participant participant;

        try {
            participant = participantsRepository.findByEventIdAndUserId(event.getId(), user.getId());

            participant.setDeletedAt(null);

            participantsRepository.update(participant);
        } catch (NoResultException e) {
            participant = new Participant();

            participant.setHost(false);
            participant.setEvent(event);
            participant.setUser(user);

            participantsRepository.save(participant);
        }

        event.getParticipants().add(participant);

        return event;
    }

    @POST
    @EventExists
    @Path("{id}/leave")
    @EventLeavePermission
    public Event leave() {
        final Participant participant = requestParamService.getParticipant();

        participant.setDeletedAt(LocalDateTime.now());

        participantsRepository.update(participant);

        final Event event = requestParamService.getEvent();

        event.getParticipants().remove(participant);

        return event;
    }
}
