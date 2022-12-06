package com.jasper.schoolbell.exceptions;

import com.jasper.schoolbell.dtos.ErrorDto;

import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(final ValidationException exception) {
        Logger.getLogger(ValidationExceptionMapper.class.getName()).log(Level.SEVERE, null, exception);

        return Response.serverError()
                .entity(new ErrorDto(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
