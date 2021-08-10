package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.service.criteria.FacultyFilterCriteria;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    Optional<Faculty> findById(long id) throws ServiceException;
    long filter(int start, int size, FacultyFilterCriteria criteria, String keyword, List<Faculty> result)
            throws ServiceException;
    long filter(int start, int size, List<Faculty> result) throws ServiceException;
    long create(Faculty faculty) throws ServiceException;
    void update(Faculty faculty) throws ServiceException;
    void delete(long id) throws ServiceException;
}
