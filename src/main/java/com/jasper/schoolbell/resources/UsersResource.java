package com.jasper.schoolbell.resources;

import com.jasper.schoolbell.dtos.EventDto;
import com.jasper.schoolbell.dtos.EventOccurrenceDto;
import com.jasper.schoolbell.dtos.UserCreateDto;
import com.jasper.schoolbell.dtos.UserDto;
import com.jasper.schoolbell.entities.Event;
import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.entities.User;
import com.jasper.schoolbell.filters.HttpStatus;
import com.jasper.schoolbell.filters.JwtAuth;
import com.jasper.schoolbell.filters.ResponseMapper;
import com.jasper.schoolbell.filters.UserExists;
import com.jasper.schoolbell.repositories.EventOccurrencesRepository;
import com.jasper.schoolbell.repositories.EventsRepository;
import com.jasper.schoolbell.repositories.UsersRepository;
import com.jasper.schoolbell.services.ModelMapperService;
import com.jasper.schoolbell.services.PasswordHashService;
import com.jasper.schoolbell.services.RequestParamService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("users")
@ResponseMapper(UserDto.class)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
   @Inject
   private UsersRepository usersRepository;

   @Inject
   private EventsRepository eventsRepository;

   @Inject
   private EventOccurrencesRepository eventOccurrencesRepository;

   @Inject
   private PasswordHashService passwordHashService;

   @Inject
   private ModelMapperService modelMapperService;

   @Inject
   private RequestParamService requestParamService;

    @POST
    @HttpStatus(Response.Status.CREATED)
    public User create(@NotNull @Valid final UserCreateDto userDto) {
        final User user = modelMapperService.map(userDto, User.class);

        user.setPassword(passwordHashService.generate(userDto.getPassword().toCharArray()));

        usersRepository.save(user);

        return user;
    }

    @GET
    @JwtAuth
    public List<User> getMany() {
        return usersRepository.findMany();
    }

    @GET
    @JwtAuth
    @Path("me")
    public User me() {
        return requestParamService.getAuthUser();
    }

    @GET
    @JwtAuth
    @UserExists
    @Path("{id}")
    public User getOne() {
        return requestParamService.getUser();
    }

    @GET
    @JwtAuth
    @UserExists
    @Path("{id}/events")
    @ResponseMapper(EventDto.WithRelations.class)
    public List<Event> getEvents() {
        return eventsRepository.findManyByParticipantUserId(requestParamService.getUser().getId());
    }

    @GET
    @JwtAuth
    @UserExists
    @Path("{id}/event-occurrences")
    @ResponseMapper(EventOccurrenceDto.WithRelations.class)
    public List<EventOccurrence> getEventOccurrences() {
        return eventOccurrencesRepository.findManyByEventParticipantUserId(requestParamService.getUser().getId());
    }
}
