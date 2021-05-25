package com.wikigreen.service.implementations;

import com.wikigreen.dao.UserDAO;
import com.wikigreen.dao.implementations.UserDAOImpl;
import com.wikigreen.model.User;
import com.wikigreen.dao.DaoException;
import com.wikigreen.service.UserService;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import javax.persistence.OptimisticLockException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private static final UserDAO USER_DAO = new UserDAOImpl();
    private static final String EXCEPTION_MESSAGE = "There is no user with id ";

    @Override
    public User save(User user) throws DaoException {
        USER_DAO.openCurrentSessionWithTransaction();
        Long id;
        Long accountId = 0L;
        if (user.getAccount() != null){
            accountId = user.getAccount().getId();
        }
        try {
            id = USER_DAO.save(user);
        } catch (ObjectNotFoundException e) {
            throw new DaoException("There is no account with id " + accountId);
        }
        USER_DAO.closeCurrentSessionWithTransaction();
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) throws DaoException {
        USER_DAO.openCurrentSessionWithTransaction();
        try {
            User oldUser = USER_DAO.getById(user.getId());
            if (oldUser == null) {
                throw new DaoException(EXCEPTION_MESSAGE + user.getId());
            }
        } finally {
            USER_DAO.closeCurrentSessionWithTransaction();
        }

        USER_DAO.openCurrentSessionWithTransaction();
        try {
            USER_DAO.update(user);
        } catch (ObjectNotFoundException e) {
            throw new DaoException("Account with id " + user.getAccount().getId() + " is not found");
        }
        USER_DAO.closeCurrentSessionWithTransaction();
        USER_DAO.openCurrentSessionWithTransaction();
        try {
            return USER_DAO.getById(user.getId());
        } finally {
            USER_DAO.openCurrentSessionWithTransaction();
        }
    }

    @Override
    public User getById(Long id) throws DaoException {
        USER_DAO.openCurrentSessionWithTransaction();

        User user = USER_DAO.getById(id);

        try{
            if (user == null) {
                throw new DaoException(EXCEPTION_MESSAGE + id);
            }
        } finally {
            USER_DAO.closeCurrentSessionWithTransaction();
        }
        return user;
    }

    @Override
    public void deleteById(Long id) throws DaoException {
        Session currentSession = USER_DAO.openCurrentSessionWithTransaction();

        User user = new User();
        user.setId(id);
        USER_DAO.deleteById(id);

        try {
            USER_DAO.getCurrentTransaction().commit();
        } catch (OptimisticLockException e) {
            throw new DaoException(EXCEPTION_MESSAGE + id);
        } finally {
            currentSession.close();
        }
    }

    @Override
    public List<User> getAll() {
        USER_DAO.openCurrentSessionWithTransaction();
        List<User> list = USER_DAO.getAll();
        USER_DAO.closeCurrentSessionWithTransaction();
        return list;
    }
}