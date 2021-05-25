package com.wikigreen.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<E, I extends Serializable>{

    Session openCurrentSession();

    Session openCurrentSessionWithTransaction();

    void closeCurrentSession();

    void closeCurrentSessionWithTransaction();

    Session getCurrentSession();

    void setCurrentSession(Session currentSession);

    Transaction getCurrentTransaction();

    void setCurrentTransaction(Transaction currentTransaction);

    I save(E e) throws DaoException;

    void update(E e);

    E getById(I id);

    void deleteById(I id);

    List<E> getAll();
}
