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

/**
 * {@code SubjectServiceImpl} class is an implementation of {@link SubjectService} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class SubjectServiceImpl implements SubjectService {
    private SubjectDao subjectDao;

    @Inject
    public SubjectServiceImpl(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    @Override
    public Optional<Subject> findById(long id) throws ServiceException {
        try {
            return subjectDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int start, int size, SubjectFilterCriteria criteria, String keyword, List<Subject> result)
            throws ServiceException {
        long totalSubjects;

        try {
            switch (criteria) {
                case NONE:
                    subjectDao.selectAll(start, size, result);
                    totalSubjects = subjectDao.selectTotalCount();
                    break;
                case NAME:
                    subjectDao.selectByName(start, size, keyword, result);
                    totalSubjects = subjectDao.selectCountByName(keyword);
                    break;
                case SHORT_NAME:
                    subjectDao.selectByShortName(start, size, keyword, result);
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
