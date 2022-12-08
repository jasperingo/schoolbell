package com.jasper.schoolbell.repositories;

import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.entities.EventOccurrence;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Transactional
public class EventOccurrencesRepository {
    @Inject
    private Configuration configuration;

    public void save(final EventOccurrence eventOccurrence) {
        configuration.getEntityManager().persist(eventOccurrence);
    }

    public EventOccurrence findById(final Long id) {
        return configuration.getEntityManager()
            .createQuery("SELECT eo FROM EventOccurrence eo JOIN FETCH eo.event WHERE eo.id = ?1", EventOccurrence.class)
            .setParameter(1, id)
            .getSingleResult();
    }

    public void update(final EventOccurrence eventOccurrence) {
        configuration.getEntityManager().merge(eventOccurrence);
    }
}
