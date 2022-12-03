package com.jasper.schoolbell.exceptions;

import com.jasper.schoolbell.dtos.ErrorDto;
import lombok.Value;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Value
    private static class ValidationError {
        String name;

        String message;

        Object value;

        public static String nameFromPath(Path propertyPath) {
            String property = null;

            for (Path.Node path: propertyPath) {
                property = path.getName();
            }

            return property;
        }
    }

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        final List<ValidationError> errors = exception.getConstraintViolations().stream()
                .map(constraintViolation -> new ValidationError(
                        ValidationError.nameFromPath(constraintViolation.getPropertyPath()),
                        constraintViolation.getMessage(),
                        constraintViolation.getInvalidValue()
                ))
                .collect(Collectors.toList());

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorDto(errors))
                .build();
    }
}
