package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeService {
    Optional<Grade> findById(long id) throws ServiceException;
    long findByStudentAndSubject(int page, int pageSize, long studentId, long subjectId, List<Grade> result)
            throws ServiceException;
    double calculateAverage(long studentId) throws ServiceException;
    double calculateAverageBySubject(long studentId, long subjectId) throws ServiceException;
    long create(Grade grade) throws ServiceException;
    void update(Grade grade) throws ServiceException;
    void delete(long id) throws ServiceException;
}