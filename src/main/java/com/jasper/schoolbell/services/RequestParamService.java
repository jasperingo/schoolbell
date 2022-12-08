package com.jasper.schoolbell.services;

import com.jasper.schoolbell.entities.Event;
import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.entities.Participant;
import com.jasper.schoolbell.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class RequestParamService {
    @Context
    private SecurityContext securityContext;

    @Context
    private HttpServletRequest httpServletRequest;

    public User getAuthUser() {
        return (User) securityContext.getUserPrincipal();
    }

    public Event getEvent() {
        return (Event) httpServletRequest.getAttribute(Event.class.getName());
    }
    public Participant getParticipant() {
        return (Participant) httpServletRequest.getAttribute(Participant.class.getName());
    }

    public EventOccurrence getEventOccurrence() {
        return (EventOccurrence) httpServletRequest.getAttribute(EventOccurrence.class.getName());
    }
}
