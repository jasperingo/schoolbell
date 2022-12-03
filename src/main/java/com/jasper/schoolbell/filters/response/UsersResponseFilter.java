package com.jasper.schoolbell.filters.response;

import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.dtos.SuccessDto;
import com.jasper.schoolbell.dtos.UserDto;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
@UsersResponse
public class UsersResponseFilter implements ContainerResponseFilter {
    @Inject
    private Configuration configuration;

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        if (containerResponseContext.getStatus() < 400) {
            final Object entity = containerResponseContext.getEntity();

            if (entity instanceof List) {
                containerResponseContext.setEntity(new SuccessDto(
                        ((List<Object>) entity).stream()
                                .map(user -> configuration.getModelMapper().map(user, UserDto.class))
                                .collect(Collectors.toList()))
                );
            } else {
                containerResponseContext.setEntity(new SuccessDto(configuration.getModelMapper().map(entity, UserDto.class)));
            }
        }
    }
}
