package com.jasper.schoolbell.repositories;

import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.entities.Alert;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class AlertsRepository {
    @Inject
    private Configuration configuration;

    public void save(final Alert alert) {
        configuration.getEntityManager().persist(alert);
    }

    public void save(final List<Alert> alerts) {
        alerts.forEach(alert -> configuration.getEntityManager().persist(alert));
    }

    public void update(final Alert alert) {
        configuration.getEntityManager().merge(alert);
    }

    public Alert findByCallSessionId(final String callSessionId) {
        return configuration.getEntityManager()
            .createQuery("SELECT a FROM Alert a WHERE a.callSessionId = ?1", Alert.class)
            .setParameter(1, callSessionId)
            .getSingleResult();
    }
}
