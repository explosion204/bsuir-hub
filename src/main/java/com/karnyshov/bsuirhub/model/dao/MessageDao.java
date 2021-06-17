package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageDao {
    List<Message> selectAll() throws DaoException;
    Optional<Message> selectById(long id) throws DaoException;
    void insert(Message message) throws DaoException;
    void update(Message message) throws DaoException;
    void delete(long id) throws DaoException;
}
