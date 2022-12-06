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
}
