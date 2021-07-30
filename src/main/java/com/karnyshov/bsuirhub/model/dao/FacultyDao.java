package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyDao extends BaseDao<Faculty> {
    void selectByName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException;
    long selectCountByName(String keyword) throws DaoException;

    void selectByShortName(int offset, int limit, String keyword, List<Faculty> result) throws DaoException;
    long selectCountByShortName(String keyword) throws DaoException;
}
