package com.wikigreen.service.implementations;

import com.wikigreen.dao.EventDAO;
import com.wikigreen.dao.implementations.EventDAOImpl;
import com.wikigreen.model.Event;
import com.wikigreen.service.EventService;
import com.wikigreen.dao.DaoException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

import javax.persistence.OptimisticLockException;
import java.util.List;

public class EventServiceImpl implements EventService {
    private static final EventDAO EVENT_DAO = new EventDAOImpl();
    private static final String EXCEPTION_MESSAGE = "There is no event with id ";


    @Override
    public Event save(Event event) throws DaoException {
        EVENT_DAO.openCurrentSessionWithTransaction();
        Long id;
        try {
            id = EVENT_DAO.save(event);
        } catch (ObjectNotFoundException e) {
            throw new DaoException("File is not found");
        }
        EVENT_DAO.closeCurrentSessionWithTransaction();
        event.setId(id);
        return event;
    }

    @Override
    public Event update(Event event) throws DaoException {
        EVENT_DAO.openCurrentSessionWithTransaction();
        Event oldEvent = EVENT_DAO.getById(event.getId());


        try {
            if (oldEvent == null) {
                throw new DaoException(EXCEPTION_MESSAGE + event.getId());
            }
        } finally {
            EVENT_DAO.closeCurrentSessionWithTransaction();
        }

        EVENT_DAO.openCurrentSessionWithTransaction();
        EVENT_DAO.update(oldEvent);
        EVENT_DAO.closeCurrentSessionWithTransaction();
        EVENT_DAO.openCurrentSessionWithTransaction();
        try {
            return EVENT_DAO.getById(event.getId());
        } finally {
            EVENT_DAO.closeCurrentSessionWithTransaction();
        }

    }

    @Override
    public Event getById(Long id) throws DaoException {
        EVENT_DAO.openCurrentSessionWithTransaction();

        Event event = EVENT_DAO.getById(id);

        EVENT_DAO.closeCurrentSessionWithTransaction();

        if (event == null) {
            throw new DaoException(EXCEPTION_MESSAGE + id);
        }

        return event;
    }

    @Override
    public void deleteById(Long id) throws DaoException {
        Session currentSession = EVENT_DAO.openCurrentSessionWithTransaction();

        Event event = new Event();
        event.setId(id);

        EVENT_DAO.deleteById(id);

        try {
            EVENT_DAO.getCurrentTransaction().commit();
        } catch (OptimisticLockException e) {
            throw new DaoException(EXCEPTION_MESSAGE + id);
        } finally {
            currentSession.close();
        }
    }

    @Override
    public List<Event> getAll() {
        EVENT_DAO.openCurrentSessionWithTransaction();
        List<Event> list = EVENT_DAO.getAll();
        EVENT_DAO.closeCurrentSessionWithTransaction();
        return list;
    }
}
