package com.jasper.schoolbell.repositories;

import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.entities.Participant;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Transactional
public class ParticipantsRepository {
    @Inject
    private Configuration configuration;

    public void save(final Participant participant) {
        configuration.getEntityManager().persist(participant);
    }
}
