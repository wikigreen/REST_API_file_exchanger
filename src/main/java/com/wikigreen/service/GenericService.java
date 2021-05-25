package com.wikigreen.service;

import com.wikigreen.dao.DaoException;

import java.io.Serializable;
import java.util.List;

public interface GenericService<E, I extends Serializable>  {
    E save(E e) throws DaoException;

    E update(E e) throws DaoException;

    E getById(I i) throws DaoException;

    void deleteById(Long id) throws DaoException;

    List<E> getAll();
}