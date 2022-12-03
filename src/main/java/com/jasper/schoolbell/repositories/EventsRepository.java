package com.jasper.schoolbell.repositories;

import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.entities.Event;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Transactional
public class EventsRepository {
    @Inject
    private Configuration configuration;

    public void save(final Event event) {
        configuration.getEntityManager().persist(event);
    }

    public Event findById(final Long id) {
        return configuration.getEntityManager()
            .createQuery("SELECT e FROM Event e JOIN FETCH e.participants WHERE e.id = ?1", Event.class)
            .setParameter(1, id)
            .getSingleResult();
    }
}
