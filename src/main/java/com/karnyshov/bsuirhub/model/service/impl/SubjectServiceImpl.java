package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.SubjectDao;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.service.SubjectService;
import com.karnyshov.bsuirhub.model.service.criteria.SubjectFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

import static com.karnyshov.bsuirhub.model.service.criteria.SubjectFilterCriteria.NONE;

@Named
public class SubjectServiceImpl implements SubjectService {
    @Inject
    private SubjectDao subjectDao;

    @Override
    public Optional<Subject> findById(long id) throws ServiceException {
        try {
            return subjectDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize, SubjectFilterCriteria criteria, String keyword, List<Subject> result)
            throws ServiceException {
        int offset = pageSize * (page - 1);
        long totalSubjects;

        try {
            switch (criteria) {
                case NONE:
                    subjectDao.selectAll(offset, pageSize, result);
                    totalSubjects = subjectDao.selectTotalCount();
                    break;
                case NAME:
                    subjectDao.selectByName(offset, pageSize, keyword, result);
                    totalSubjects = subjectDao.selectCountByName(keyword);
                    break;
                case SHORT_NAME:
                    subjectDao.selectByShortName(offset, pageSize, keyword, result);
                    totalSubjects = subjectDao.selectCountByShortName(keyword);
                    break;
                default:
                    throw new ServiceException("Invalid criteria: " + criteria);
            }

            return totalSubjects;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize, List<Subject> result) throws ServiceException {
        return filter(page, pageSize, NONE, null, result);
    }

    @Override
    public long create(Subject subject) throws ServiceException {
        try {
            return subjectDao.insert(subject);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Subject subject) throws ServiceException {
        try {
            subjectDao.update(subject);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            subjectDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}