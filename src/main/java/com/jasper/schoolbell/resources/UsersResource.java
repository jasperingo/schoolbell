package com.jasper.schoolbell.resources;

import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.dtos.UserCreateDto;
import com.jasper.schoolbell.entities.User;
import com.jasper.schoolbell.filters.response.UsersResponse;
import com.jasper.schoolbell.repositories.UsersRepository;
import com.jasper.schoolbell.services.PasswordHashService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("users")
@UsersResponse
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
   @Inject
   private UsersRepository usersRepository;

   @Inject
   private PasswordHashService passwordHashService;

   @Inject
   private Configuration configuration;

    @POST
    public Response create(@NotNull @Valid final UserCreateDto userDto) throws URISyntaxException {
        final User user = configuration.getModelMapper().map(userDto, User.class);

        user.setPassword(passwordHashService.generate(userDto.getPassword().toCharArray()));

        usersRepository.save(user);

        return Response.created(new URI("")).entity(user).build();
    }
}
