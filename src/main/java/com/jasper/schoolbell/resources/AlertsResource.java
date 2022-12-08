package com.jasper.schoolbell.resources;

import com.jasper.schoolbell.dtos.CallDto;
import com.jasper.schoolbell.entities.Alert;
import com.jasper.schoolbell.repositories.AlertsRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Objects;

@Path("alerts")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class AlertsResource {
    @Inject
    private AlertsRepository alertsRepository;

    @POST
    public CallDto start(
        @FormParam("sessionId") final String callSessionId,
        @FormParam("isActive") final String isActive,
        @FormParam("amount") final Double amount,
        @FormParam("durationInSeconds") final Long duration,
        @FormParam("hangupCause") final String status
    ) {
        try {
            final Alert alert = alertsRepository.findByCallSessionId(callSessionId);

            if (Objects.equals(isActive, "0")) {
                alert.setCost(amount);
                alert.setActive(false);
                alert.setDuration(duration);
                alert.setStatus(status == null ? "Done" : status);

                alertsRepository.update(alert);

                return null;
            }

            return CallDto.buildSay(
                String.format(
                    "Hello %s %s, I am School bell. %s",
                    alert.getParticipant().getUser().getFirstName(),
                    alert.getParticipant().getUser().getLastName(),
                    alert.getMessage()
                )
            );
        } catch (NoResultException e) {
            return CallDto.buildSay("Sorry, this is an invalid call.");
        }
    }
}
