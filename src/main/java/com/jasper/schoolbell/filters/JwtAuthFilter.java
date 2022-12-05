package com.jasper.schoolbell.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jasper.schoolbell.entities.User;
import com.jasper.schoolbell.repositories.UsersRepository;
import com.jasper.schoolbell.services.JwtService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

@JwtAuth
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {
    public static final String REALM = "example";
    public static final String AUTHENTICATION_SCHEME = "Bearer";

    @Inject
    private JwtService jwtService;

    @Inject
    private UsersRepository usersRepository;

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        try {
            if (!isTokenBasedAuthentication(authorizationHeader)) {
                throw new IllegalArgumentException();
            }

            String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();


            DecodedJWT decodedJWT = jwtService.verifyToken(token);

            final User user = usersRepository.findById(Long.parseLong(decodedJWT.getSubject()));

            if (user == null) {
                throw new IllegalArgumentException();
            }

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

        } catch (JWTVerificationException | IllegalArgumentException e) {
            throw new NotAuthorizedException("Access token is invalid", "");
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase()
            .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }
}
