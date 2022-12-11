package com.jasper.schoolbell.resources;

import com.jasper.schoolbell.dtos.AuthCreateDto;
import com.jasper.schoolbell.dtos.AuthDto;
import com.jasper.schoolbell.dtos.SuccessDto;
import com.jasper.schoolbell.entities.User;
import com.jasper.schoolbell.filters.HttpStatus;
import com.jasper.schoolbell.repositories.UsersRepository;
import com.jasper.schoolbell.services.JwtService;
import com.jasper.schoolbell.services.PasswordHashService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    private UsersRepository usersRepository;

    @Inject
    private PasswordHashService passwordHashService;

    @Inject
    private JwtService jwtService;

    @POST
    @HttpStatus(Response.Status.OK)
    public SuccessDto auth(final AuthCreateDto authDto) {
        try {
            if (authDto.getPassword() == null || authDto.getPhoneNumber() == null) {
                throw new NotAuthorizedException("");
            }

            final User user = usersRepository.findByPhoneNumber(authDto.getPhoneNumber());

            if (!passwordHashService.verify(authDto.getPassword().toCharArray(), user.getPassword())) {
                throw new NotAuthorizedException("");
            }

            return new SuccessDto(new AuthDto(
                jwtService.generateToken(user),
                jwtService.getExpirationDate()
            ));
        } catch (NoResultException | NotAuthorizedException e) {
            throw new NotAuthorizedException("Credentials are incorrect", "");
        }
    }
}
