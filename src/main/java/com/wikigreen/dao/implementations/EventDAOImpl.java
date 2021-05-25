package com.wikigreen.dao.implementations;

import com.wikigreen.dao.EventDAO;
import com.wikigreen.model.Account;
import com.wikigreen.model.Event;
import com.wikigreen.model.File;
import com.wikigreen.model.User;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDAOImpl extends SessionAndTransactionAccess implements EventDAO {
    @Override
    public Long save(Event event) {
        if(event.getFile() != null && event.getFile().getId() != null){
            event.setFile(
                currentSession.load(File.class, event.getFile().getId())
            );
            event.getFile().getOwner().getEvents().add(event);
        }
        event.setDate(new Date());
        return (Long) currentSession.save(event);
    }

    @Override
    public void update(Event event) {
        Event preUpdEvent = currentSession.load(Event.class, event.getId());

        if(event.getFile() != null) {
            preUpdEvent.setFile(event.getFile());
        }

        if (event.getEventType() != null){
            preUpdEvent.setEventType(event.getEventType());
        }

        currentSession.update(preUpdEvent);
    }

    @Override
    public Event getById(Long id){
        try {
            Event event = currentSession.createQuery(
                    "SELECT e FROM Event e " +
                            "LEFT JOIN FETCH e.file " +
                            "WHERE e.id = :id", Event.class)
                    .setParameter("id", id)
                    .getSingleResult();

            File lazyLoadedFile = event.getFile();
            initializeFile(lazyLoadedFile);
            event.setFile(lazyLoadedFile);

            return event;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public void deleteById(Long id){
        Event event = new Event();
        event.setId(id);
        currentSession.delete(event);
    }

    @Override
    public List<Event> getAll() {
        List<Event> resultList = currentSession.createQuery(
                "SELECT e FROM Event e " +
                        "LEFT JOIN FETCH e.file", Event.class)
                .getResultList();

        resultList.stream()
                .map(Event::getFile)
                .forEach(EventDAOImpl::initializeFile);

        return resultList;
    }

    static void initializeFile(File file) {
        if (file == null) return;
        User lazyLoadedUser = file.getOwner();

        if (lazyLoadedUser == null) {
            file.setOwner(null);
            return;
        }

        List<File> files = new ArrayList<>(lazyLoadedUser.getFiles());
        List<Event> events = new ArrayList<>(lazyLoadedUser.getEvents());

        Account lazyLoadedAccount = lazyLoadedUser.getAccount();
        if (lazyLoadedAccount != null){
            lazyLoadedAccount = new Account(lazyLoadedUser.getAccount());
        }

        file.setOwner(new User(
                lazyLoadedUser.getId(),
                lazyLoadedAccount,
                files,
                events,
                lazyLoadedUser.getFirstName(),
                lazyLoadedUser.getLastName()
        ));
    }
}
