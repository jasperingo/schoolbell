package com.jasper.schoolbell.repositories;

import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.entities.Event;
import com.jasper.schoolbell.entities.EventOccurrence;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class EventOccurrencesRepository {
    @Inject
    private Configuration configuration;

    public void save(final EventOccurrence eventOccurrence) {
        configuration.getEntityManager().persist(eventOccurrence);
    }

    public void update(final EventOccurrence eventOccurrence) {
        configuration.getEntityManager().merge(eventOccurrence);
    }

    public EventOccurrence findById(final Long id) {
        return configuration.getEntityManager()
            .createQuery("SELECT eo FROM EventOccurrence eo JOIN FETCH eo.event WHERE eo.id = ?1", EventOccurrence.class)
            .setParameter(1, id)
            .getSingleResult();
    }

    public List<EventOccurrence> findManyByEventParticipantUserId(final Long userId) {
        return configuration.getEntityManager()
            .createQuery("SELECT eo FROM EventOccurrence eo JOIN eo.event e LEFT JOIN e.participants pa LEFT JOIN pa.user u WHERE u.id = ?1", EventOccurrence.class)
            .setParameter(1, userId)
            .getResultStream()
            .peek(eventOccurrence -> eventOccurrence.setEvent(
                configuration.getEntityManager()
                    .createQuery("SELECT eo.event FROM EventOccurrence eo JOIN FETCH eo.event WHERE eo.id = ?1", Event.class)
                    .setParameter(1, eventOccurrence.getId())
                    .getSingleResult()
            ))
            .collect(Collectors.toList());
    }
}
