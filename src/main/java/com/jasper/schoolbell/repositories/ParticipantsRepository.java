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

    public void update(final Participant participant) {
        configuration.getEntityManager().merge(participant);
    }

    public Participant findByEventIdAndUserId(final Long eventId, final Long userId) {
        return configuration.getEntityManager()
            .createQuery("SELECT p FROM Participant p WHERE p.event.id = ?1 AND p.user.id = ?2", Participant.class)
            .setParameter(1, eventId)
            .setParameter(2, userId)
            .getSingleResult();
    }

}
