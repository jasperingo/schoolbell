package com.jasper.schoolbell.exceptions;

import com.jasper.schoolbell.dtos.ErrorDto;
import javax.ws.rs.NotFoundException;
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
            (exception instanceof NotFoundException)
                ? Response.status(Response.Status.NOT_FOUND)
                : Response.serverError();

        Logger.getLogger(AllExceptionMapper.class.getName()).log(Level.SEVERE, null, exception);

        return responseBuilder
                .entity(new ErrorDto(exception.getMessage()))
                .build();
    }
}
