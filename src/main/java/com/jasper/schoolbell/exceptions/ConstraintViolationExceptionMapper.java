package com.jasper.schoolbell.exceptions;

import com.jasper.schoolbell.dtos.ErrorDto;
import com.jasper.schoolbell.validators.ValidationPayload;
import lombok.Value;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Payload;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Value
    private static class ValidationError {
        String name;

        String message;

        Object value;

        public static String nameFromPath(ConstraintViolation<?> constraintViolation) {
            final Set<Class<? extends Payload>> payloadSet = constraintViolation.getConstraintDescriptor().getPayload();

            final Optional<Class<? extends Payload>> payloadOptional = payloadSet.stream()
                    .filter(ValidationPayload.class::isAssignableFrom)
                    .findFirst();

            if (payloadOptional.isPresent()) {
                try {
                    return ((ValidationPayload) payloadOptional.get().getConstructor().newInstance()).getName();
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            final Path propertyPath = constraintViolation.getPropertyPath();

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
                    ValidationError.nameFromPath(constraintViolation),
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
