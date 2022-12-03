package com.jasper.schoolbell.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jasper.schoolbell.dtos.ErrorDto;
import com.jasper.schoolbell.entities.User;
import com.jasper.schoolbell.repositories.UsersRepository;
import com.jasper.schoolbell.services.JwtService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

@Provider
@JwtAuth
public class JwtAuthFilter implements ContainerRequestFilter {
    private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Inject
    private JwtService jwtService;

    @Inject
    private UsersRepository usersRepository;

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            DecodedJWT decodedJWT = jwtService.verifyToken(token);

            final User user = usersRepository.findById(Long.parseLong(decodedJWT.getSubject()));

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return user;
                }

                @Override
                public boolean isUserInRole(String s) {
                    return true;
                }

                @Override
                public boolean isSecure() {
                    return requestContext.getSecurityContext().isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return AUTHENTICATION_SCHEME;
                }
            });

        } catch (JWTVerificationException | NoResultException e) {
            abortWithUnauthorized(requestContext);
        } catch (Exception e) {
            abortWithServerError(requestContext);
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase()
            .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
                .entity(new ErrorDto("Access token is invalid"))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()
        );
    }

    private void abortWithServerError(ContainerRequestContext requestContext) {
        requestContext.abortWith(
            Response.serverError()
                .entity(new ErrorDto("An error occurred"))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build()
        );
    }
}
