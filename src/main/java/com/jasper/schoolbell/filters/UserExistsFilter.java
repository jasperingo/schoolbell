package com.jasper.schoolbell.filters;

import com.jasper.schoolbell.entities.User;
import com.jasper.schoolbell.repositories.UsersRepository;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@UserExists
@Priority(500)
public class UserExistsFilter implements ContainerRequestFilter {
    @Inject
    private UsersRepository usersRepository;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        final String id = containerRequestContext.getUriInfo().getPathParameters().getFirst("id");

        try {
            final User user = usersRepository.findById(Long.parseLong(id));

            containerRequestContext.setProperty(User.class.getName(), user);
        } catch (NoResultException e) {
            throw new NotFoundException("User not found");
        }
    }
}
