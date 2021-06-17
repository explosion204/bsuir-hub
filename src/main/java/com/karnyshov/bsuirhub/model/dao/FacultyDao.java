package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyDao {
    List<Faculty> selectAll() throws DaoException;
    Optional<Faculty> selectById(long id) throws DaoException;
    void insert(Faculty faculty) throws DaoException;
    void update(Faculty faculty) throws DaoException;
}
