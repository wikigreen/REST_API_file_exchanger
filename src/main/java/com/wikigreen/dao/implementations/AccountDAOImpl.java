package com.wikigreen.dao.implementations;

import com.wikigreen.dao.AccountDAO;
import com.wikigreen.dao.DaoException;
import com.wikigreen.model.Account;
import com.wikigreen.model.Event;
import com.wikigreen.model.File;
import com.wikigreen.model.User;
import org.hibernate.annotations.QueryHints;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

public class AccountDAOImpl extends SessionAndTransactionAccess implements AccountDAO {
    @Override
    public Long save(Account account) throws DaoException {
        if (account.getUser() != null) {
            User user;
            if (account.getUser().getId() != null) {
                user = currentSession.load(User.class, account.getUser().getId());
            } else {
                user = account.getUser();
                if (account.getUser() != null && account.getUser().getFiles() != null) {
                    account.getUser().getFiles().stream()
                            .filter(file -> file.getId() == null)
                            .forEach(file -> {
                                file.setUploadDate(new Date());
                                file.setUpdateDate(file.getUploadDate());
                            });
                }
            }
            account.setUser(user);
            account.getUser().setAccount(account);
        }

        return (Long) currentSession.save(account);
    }

    @Override
    public void update(Account account) {
        Account preUpdAccount = currentSession.load(Account.class, account.getId());

        if (account.getUser() != null) {
            preUpdAccount.setUser(currentSession.load(User.class, account.getUser().getId()));
            preUpdAccount.getUser().setAccount(preUpdAccount);
        }

        if (account.getNickName() != null) {
            preUpdAccount.setNickName(account.getNickName());
        }

        if (account.getAccountStatus() != null) {
            preUpdAccount.setAccountStatus(account.getAccountStatus());
        }

        if (account.getEmail() != null) {
            preUpdAccount.setEmail(account.getEmail());
        }

        currentSession.update(preUpdAccount);
    }

    @Override
    public Account getById(Long id) {
        try {
            Account account = currentSession.createQuery(
                    "SELECT a FROM Account a " +
                            "LEFT JOIN FETCH a.user " +
                            "WHERE a.id = :id", Account.class)
                    .setParameter("id", id)
                    .getSingleResult();
            setListsForUser(account.getUser());
            return account;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        Account account = new Account();
        account.setId(id);
        currentSession.delete(account);
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = currentSession.createQuery(
                "SELECT DISTINCT a FROM Account a " +
                        "LEFT JOIN a.user u", Account.class)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getResultList();

        accounts.forEach(account -> setListsForUser(account.getUser()));

        return accounts;
    }

    private void setListsForUser(User user) {
        if (user == null) {
            return;
        }
        Account account = user.getAccount();
        account.getUser().setFiles(
                currentSession.createQuery(
                        "SELECT f FROM User u " +
                                "RIGHT JOIN u.files f " +
                                "WHERE u.id = :id", File.class)
                        .setParameter("id", account.getUser().getId())
                        .getResultList()
        );
        account.getUser().setEvents(
                currentSession.createQuery(
                        "SELECT e FROM User u " +
                                "RIGHT JOIN u.events e " +
                                "WHERE u.id = :id", Event.class)
                        .setParameter("id", account.getUser().getId())
                        .getResultList()
        );
    }
}