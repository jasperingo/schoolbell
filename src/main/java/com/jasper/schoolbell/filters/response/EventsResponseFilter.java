package com.jasper.schoolbell.filters.response;

import com.jasper.schoolbell.dtos.EventDto;
import com.jasper.schoolbell.dtos.SuccessDto;
import com.jasper.schoolbell.dtos.UserDto;
import com.jasper.schoolbell.services.ModelMapperService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
@EventsResponse
public class EventsResponseFilter implements ContainerResponseFilter {
    @Inject
    private ModelMapperService modelMapperService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        if (containerResponseContext.getStatus() < 400) {
            final Object entity = containerResponseContext.getEntity();

            if (entity instanceof List) {
                containerResponseContext.setEntity(new SuccessDto(
                    modelMapperService.map((List<?>) entity, EventDto.class)
                ));
            } else {
                containerResponseContext.setEntity(new SuccessDto(
                    modelMapperService.map(entity, EventDto.class)
                ));
            }
        }
    }
}
