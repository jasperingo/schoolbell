package com.jasper.schoolbell.repositories;

import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.entities.Event;
import com.jasper.schoolbell.entities.EventOccurrence;
import com.jasper.schoolbell.entities.Participant;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class EventsRepository {
    @Inject
    private Configuration configuration;

    public void save(final Event event) {
        configuration.getEntityManager().persist(event);
    }

    public List<Event> findMany() {
        return configuration.getEntityManager()
            .createQuery("SELECT e FROM Event e", Event.class)
            .getResultList()
            .stream()
            .peek(event -> event.setParticipants(
                configuration.getEntityManager()
                    .createQuery("SELECT p FROM Participant p WHERE p.event.id = ?1 AND p.host = true", Participant.class)
                    .setParameter(1, event.getId())
                    .getResultList()
            ))
            .peek(event -> event.setEventOccurrences(
                configuration.getEntityManager()
                    .createQuery("SELECT eo FROM EventOccurrence eo WHERE eo.event.id = ?1 AND eo.startedAt >= ?2 ORDER BY eo.startedAt ASC", EventOccurrence.class)
                    .setParameter(1, event.getId())
                    .setParameter(2, LocalDateTime.now())
                    .setMaxResults(1)
                    .getResultList()
            )).
            collect(Collectors.toList());
    }

    public Event findById(final Long id) {
        return configuration.getEntityManager()
            .createQuery("SELECT e FROM Event e JOIN FETCH e.participants LEFT JOIN FETCH e.eventOccurrences WHERE e.id = ?1", Event.class)
            .setParameter(1, id)
            .getSingleResult();
    }
}
