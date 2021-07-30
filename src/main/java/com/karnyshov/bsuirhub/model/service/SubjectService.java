package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.service.criteria.SubjectFilterCriteria;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    Optional<Subject> findById(long id) throws ServiceException;
    long filter(int page, int pageSize, SubjectFilterCriteria criteria, String keyword, List<Subject> result)
            throws ServiceException;
    long filter(int page, int pageSize, List<Subject> result) throws ServiceException;
    long create(Subject subject) throws ServiceException;
    void update(Subject subject) throws ServiceException;
    void delete(long id) throws ServiceException;
}
