package com.wikigreen.service.implementations;

import com.wikigreen.dao.AccountDAO;
import com.wikigreen.dao.implementations.AccountDAOImpl;
import com.wikigreen.model.Account;
import com.wikigreen.service.AccountService;
import com.wikigreen.dao.DaoException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

import javax.persistence.OptimisticLockException;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    private static final AccountDAO ACCOUNT_DAO = new AccountDAOImpl();
    private static final String EXCEPTION_MESSAGE = "There is no account with id ";

    @Override
    public Account save(Account account) throws DaoException {
        ACCOUNT_DAO.openCurrentSessionWithTransaction();
        Long id;
        Long userID = null;
        if (account.getUser() != null) {
            userID = account.getUser().getId();
        }
        try {
            id = ACCOUNT_DAO.save(account);
        } catch (ObjectNotFoundException e) {
            throw new DaoException("There is no user with id " + userID);
        }
        ACCOUNT_DAO.closeCurrentSessionWithTransaction();
        account.setId(id);
        return account;
    }

    @Override
    public Account update(Account account) throws DaoException {
        ACCOUNT_DAO.openCurrentSessionWithTransaction();
        Account oldAccount = ACCOUNT_DAO.getById(account.getId());

        try {
            if (oldAccount == null) {
                throw new DaoException(EXCEPTION_MESSAGE + account.getId());
            }
        } finally {
            ACCOUNT_DAO.closeCurrentSessionWithTransaction();
        }

        ACCOUNT_DAO.openCurrentSessionWithTransaction();
        try {
            ACCOUNT_DAO.update(account);
        } catch (ObjectNotFoundException e) {
            throw new DaoException("User with id " + account.getUser().getId() + " is not found");
        }
        ACCOUNT_DAO.closeCurrentSessionWithTransaction();

        ACCOUNT_DAO.openCurrentSessionWithTransaction();
        Account newAccount = ACCOUNT_DAO.getById(account.getId());
        ACCOUNT_DAO.closeCurrentSessionWithTransaction();
        return newAccount;
    }

    @Override
    public Account getById(Long id) throws DaoException {
        ACCOUNT_DAO.openCurrentSessionWithTransaction();

        Account account = ACCOUNT_DAO.getById(id);

        ACCOUNT_DAO.closeCurrentSessionWithTransaction();

        if (account == null) {
            throw new DaoException(EXCEPTION_MESSAGE + id);
        }

        return account;
    }

    @Override
    public void deleteById(Long id) throws DaoException {
        Session currentSession = ACCOUNT_DAO.openCurrentSessionWithTransaction();

        Account account = new Account();
        account.setId(id);

        ACCOUNT_DAO.deleteById(id);

        try {
            ACCOUNT_DAO.getCurrentTransaction().commit();
        } catch (OptimisticLockException e) {
            throw new DaoException(EXCEPTION_MESSAGE + id);
        } finally {
            currentSession.close();
        }
    }

    @Override
    public List<Account> getAll() {
        ACCOUNT_DAO.openCurrentSessionWithTransaction();
        List<Account> list = ACCOUNT_DAO.getAll();
        ACCOUNT_DAO.closeCurrentSessionWithTransaction();
        return list;
    }
}