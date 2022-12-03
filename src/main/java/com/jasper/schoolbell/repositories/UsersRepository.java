package com.jasper.schoolbell.repositories;

import com.jasper.schoolbell.Configuration;
import com.jasper.schoolbell.entities.User;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class UsersRepository {
    @Inject
    private Configuration configuration;

    public void save(final User user) {
        configuration.getEntityManager().persist(user);
    }

    public List<User> findMany() {
        return configuration.getEntityManager()
            .createQuery("SELECT u FROM User u", User.class)
            .getResultList();
    }

    public User findById(final Long id) {
        return configuration.getEntityManager().find(User.class, id);
    }

    public User findByPhoneNumber(final String phoneNumber) {
        return configuration.getEntityManager()
            .createQuery("SELECT u FROM User u WHERE u.phoneNumber = ?1", User.class)
            .setParameter(1, phoneNumber)
            .getSingleResult();
    }

    public boolean existsByPhoneNumber(final String phoneNumber) {
       return !configuration.getEntityManager()
           .createQuery("SELECT u FROM User u WHERE u.phoneNumber = ?1", User.class)
           .setParameter(1, phoneNumber)
           .getResultList()
           .isEmpty();
    }
}
