package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao {
    List<Group> selectAll() throws DaoException;
    Optional<Group> selectById(long id) throws DaoException;
    void insert(Group group) throws DaoException;
    void update(Group group) throws DaoException;
}
