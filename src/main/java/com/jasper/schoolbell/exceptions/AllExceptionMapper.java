package com.jasper.schoolbell.exceptions;

import com.jasper.schoolbell.dtos.ErrorDto;
import com.jasper.schoolbell.filters.JwtAuthFilter;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class AllExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(final Exception exception) {
        Response.ResponseBuilder responseBuilder =
            (exception instanceof WebApplicationException)
                ? Response.status(((WebApplicationException) exception).getResponse().getStatus())
                : Response.serverError();

        if (exception instanceof NotAuthorizedException) {
            responseBuilder = responseBuilder.header(
                HttpHeaders.WWW_AUTHENTICATE,
                JwtAuthFilter.AUTHENTICATION_SCHEME + " realm=\"" + JwtAuthFilter.REALM + "\""
            );
        }

        Logger.getLogger(AllExceptionMapper.class.getName()).log(Level.SEVERE, null, exception);

        return responseBuilder
                .entity(new ErrorDto(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
