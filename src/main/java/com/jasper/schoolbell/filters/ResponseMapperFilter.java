package com.jasper.schoolbell.filters;

import com.jasper.schoolbell.dtos.SuccessDto;
import com.jasper.schoolbell.services.ModelMapperService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Provider
@ResponseMapper(Object.class)
public class ResponseMapperFilter implements ContainerResponseFilter {
    @Inject
    private ModelMapperService modelMapperService;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        if (containerResponseContext.getStatus() < 400) {
            final Optional<Annotation> annotationOptional = Arrays.stream(resourceInfo.getResourceMethod().getAnnotations())
                    .filter(annotation1 -> annotation1 instanceof ResponseMapper)
                    .findFirst();

            if (annotationOptional.isPresent()) {
                map(containerResponseContext, annotationOptional.get());
            } else {
                Arrays.stream(resourceInfo.getResourceClass().getAnnotations())
                    .filter(annotation1 -> annotation1 instanceof ResponseMapper)
                    .findFirst()
                    .ifPresent(annotation -> map(containerResponseContext, annotation));
            }
        }
    }

    private void map(final ContainerResponseContext containerResponseContext, final Annotation annotation) {
        final Object entity = containerResponseContext.getEntity();

        final ResponseMapper responseMapper = (ResponseMapper) annotation;

        containerResponseContext.setEntity(new SuccessDto(
            (entity instanceof List)
                ? modelMapperService.map((List<?>) entity, responseMapper.value())
                : modelMapperService.map(entity, responseMapper.value())
        ));
    }
}
