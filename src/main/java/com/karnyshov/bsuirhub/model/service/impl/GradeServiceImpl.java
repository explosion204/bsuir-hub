package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.GradeDao;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.service.GradeService;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class GradeServiceImpl implements GradeService {
    @Inject
    private GradeDao gradeDao;

    @Override
    public Optional<Grade> findById(long id) throws ServiceException {
        try {
            return gradeDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long findByStudentAndSubject(int start, int size, long studentId, long subjectId, List<Grade> result)
            throws ServiceException {
        try {
            gradeDao.selectByStudentAndSubject(start, size, studentId, subjectId, result);
            return gradeDao.selectCountByStudentAndSubject(studentId, subjectId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public double calculateAverage(long studentId) throws ServiceException {
        try {
            return gradeDao.selectAverage(studentId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public double calculateAverageBySubject(long studentId, long subjectId) throws ServiceException {
        try {
            return gradeDao.selectAverageBySubject(studentId, subjectId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long create(Grade grade) throws ServiceException {
        try {
            return gradeDao.insert(grade);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Grade grade) throws ServiceException {
        try {
            gradeDao.update(grade);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            gradeDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
