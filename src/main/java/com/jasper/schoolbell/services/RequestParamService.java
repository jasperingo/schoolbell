package com.jasper.schoolbell.services;

import com.jasper.schoolbell.entities.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class RequestParamService {
    @Context
    private SecurityContext securityContext;

    public User getAuthUser() {
        return (User) securityContext.getUserPrincipal();
    }
}
