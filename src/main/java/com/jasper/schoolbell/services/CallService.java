package com.jasper.schoolbell.services;

import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.dtos.CallCreateDto;
import com.jasper.schoolbell.entities.Alert;
import com.jasper.schoolbell.entities.Event;
import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.repositories.AlertsRepository;
import com.jasper.schoolbell.repositories.EventsRepository;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.Objects;
import java.util.stream.Collectors;

@Stateless
public class CallService {
    @Inject
    private Configuration configuration;

    @Inject
    private EventsRepository eventsRepository;

    @Inject
    private AlertsRepository alertsRepository;

    private CallCreateDto send(final String recipientPhoneNumber) {
        final Client client = ClientBuilder.newClient();

        final Form form = new Form();

        form.param("username", configuration.getAfricansTalkingAppUsername());
        form.param("from", configuration.getAfricansTalkingPhoneNumber());
        form.param("to", recipientPhoneNumber);

        return client.target(configuration.getAfricansTalkingApiEndPoint())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("apiKey", configuration.getAfricansTalkingApiKey())
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), CallCreateDto.class);
    }

    @Asynchronous
    public void sendCall(final EventOccurrence eventOccurrence, final String message) {
        final Event event = eventsRepository.findById(eventOccurrence.getEvent().getId());

        final String phoneNumbers = event.getParticipants()
            .stream()
            .filter(participant -> !participant.isHost())
            .map(participant ->
                Objects.equals(
                    participant.getId(),
                    event.getParticipants().get(event.getParticipants().size() - 1).getId()
                )
                    ? participant.getUser().getPhoneNumber()
                    : participant.getUser().getPhoneNumber()+","
            )
            .collect(Collectors.joining());

        alertsRepository.save(send(phoneNumbers).getEntries()
            .stream()
            .map(entry -> {
                final Alert alert = new Alert();
                alert.setActive(true);
                alert.setMessage(message);
                alert.setEventOccurrence(eventOccurrence);
                alert.setCallSessionId(entry.getSessionId());
                alert.setStatus(entry.getStatus());
                alert.setParticipant(
                    event.getParticipants()
                        .stream()
                        .filter(participant -> Objects.equals(participant.getUser().getPhoneNumber(), entry.getPhoneNumber()))
                        .findFirst()
                        .orElse(null)
                );
                return alert;
            })
            .collect(Collectors.toList())
        );
    }
}
