package com.wikigreen.dao.implementations;

import com.wikigreen.dao.UserDAO;
import com.wikigreen.model.Account;
import com.wikigreen.model.Event;
import com.wikigreen.model.File;
import com.wikigreen.model.User;
import org.hibernate.StaleStateException;
import org.hibernate.annotations.QueryHints;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDAOImpl extends SessionAndTransactionAccess implements UserDAO {
    @Override
    public Long save(User user) {
        if (user.getAccount() != null && user.getAccount().getId() != null) {
            Account account;
            if (user.getAccount().getId() != null) {
                account = currentSession.load(Account.class, user.getAccount().getId());
            } else {
                account = user.getAccount();
            }
            user.setAccount(account);
            user.getAccount().setUser(user);
        }

        user.setFiles(
                user.getFiles().stream()
                        .map(file -> {
                            file.setOwner(user);
                            return file;
                        })
                        .map(file -> {
                            if (file.getId() != null) {
                                return currentSession.get(File.class, file.getId());
                            }
                            return file;
                        })
                        .collect(Collectors.toList())

        );

        user.getFiles().stream()
                .filter(file -> file.getId() == null)
                .forEach(file -> {
                    file.setUploadDate(new Date());
                    file.setUpdateDate(file.getUploadDate());
                });

        user.setEvents(
                user.getEvents().stream()
                        .map(event -> {
                            if (event.getId() != null) {
                                return currentSession.get(Event.class, event.getId());
                            }
                            return event;
                        })
                        .collect(Collectors.toList())
        );

        return (Long) currentSession.save(user);
    }

    @Override
    public void update(User user) {
        User preUpdUser = currentSession.load(User.class, user.getId());

        if (user.getEvents() != null) {
            preUpdUser.setEvents(user.getEvents());
        }

        if (user.getAccount() != null) {
            preUpdUser.setAccount(currentSession.load(Account.class, user.getAccount().getId()));
            preUpdUser.getAccount().setUser(preUpdUser);
        }

        if (user.getLastName() != null) {
            preUpdUser.setLastName(user.getLastName());
        }

        if (user.getFirstName() != null) {
            preUpdUser.setFirstName(user.getFirstName());
        }

        closeCurrentSessionWithTransaction();
        openCurrentSessionWithTransaction();

        currentSession.update(preUpdUser);
    }

    @Override
    public User getById(Long id) {
        User user;
        try {
            user = currentSession
                    .createQuery(
                            "SELECT u FROM User u " +
                                    "LEFT JOIN FETCH u.account " +
                                    "where u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        user.setEvents(
                currentSession.createQuery(
                        "SELECT e FROM User u " +
                                "RIGHT JOIN u.events e " +
                                "WHERE u.id = :id", Event.class)
                        .setParameter("id", id)
                        .getResultList()
        );
        user.setFiles(
                currentSession.createQuery(
                        "SELECT f FROM User u " +
                                "RIGHT JOIN u.files f " +
                                "WHERE u.id = :id", File.class)
                        .setParameter("id", id)
                        .getResultList()
        );
        return user;
    }

    @Override
    public void deleteById(Long id) throws StaleStateException {
        User user = new User();
        user.setId(id);
        currentSession.delete(user);
    }

    @Override
    public List<User> getAll() {
        List<User> users = currentSession
                .createQuery(
                        "SELECT DISTINCT u From User u " +
                                "LEFT JOIN FETCH u.account " +
                                "LEFT JOIN FETCH u.events", User.class)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getResultList();

        users = currentSession
                .createQuery(
                        "SELECT DISTINCT u " +
                                "FROM User u " +
                                "LEFT JOIN FETCH u.files " +
                                "WHERE u in :users", User.class)
                .setParameter("users", users)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getResultList();

        return users;
    }
}