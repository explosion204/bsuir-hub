package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.StudyAssignmentDao;
import com.karnyshov.bsuirhub.model.entity.StudyAssignment;
import com.karnyshov.bsuirhub.model.service.StudyAssignmentService;
import com.karnyshov.bsuirhub.model.service.criteria.StudyAssignmentFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class StudyAssignmentServiceImpl implements StudyAssignmentService {
    @Inject
    private StudyAssignmentDao studyAssignmentDao;

    @Override
    public Optional<StudyAssignment> findById(long id) throws ServiceException {
        try {
            return studyAssignmentDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize,  StudyAssignmentFilterCriteria criteria, long filterId,
                List<StudyAssignment> result) throws ServiceException {
        int offset = pageSize * (page - 1);
        long totalSubjects;

        try {
            switch (criteria) {
                case GROUP:
                    studyAssignmentDao.selectByGroup(offset, pageSize, filterId, result);
                    totalSubjects = studyAssignmentDao.selectCountByGroup(filterId);
                    break;
                case TEACHER:
                    studyAssignmentDao.selectByTeacher(offset, pageSize, filterId, result);
                    totalSubjects = studyAssignmentDao.selectCountByTeacher(filterId);
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
    public long create(StudyAssignment studyAssignment) throws ServiceException {
        try {
            return studyAssignmentDao.insert(studyAssignment);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(StudyAssignment studyAssignment) throws ServiceException {
        try {
            studyAssignmentDao.update(studyAssignment);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            studyAssignmentDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
