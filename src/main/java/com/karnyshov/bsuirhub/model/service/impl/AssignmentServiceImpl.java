package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.AssignmentDao;
import com.karnyshov.bsuirhub.model.entity.Assignment;
import com.karnyshov.bsuirhub.model.service.AssignmentService;
import com.karnyshov.bsuirhub.model.service.criteria.StudyAssignmentFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class AssignmentServiceImpl implements AssignmentService {
    @Inject
    private AssignmentDao assignmentDao;

    @Override
    public Optional<Assignment> findById(long id) throws ServiceException {
        try {
            return assignmentDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int start, int size, StudyAssignmentFilterCriteria criteria, long filterId,
                       List<Assignment> result) throws ServiceException {
        long totalAssignments;

        try {
            switch (criteria) {
                case GROUP:
                    assignmentDao.selectByGroup(start, size, filterId, result);
                    totalAssignments = assignmentDao.selectCountByGroup(filterId);
                    break;
                case TEACHER:
                    assignmentDao.selectByTeacher(start, size, filterId, result);
                    totalAssignments = assignmentDao.selectCountByTeacher(filterId);
                    break;
                default:
                    throw new ServiceException("Invalid criteria: " + criteria);
            }

            return totalAssignments;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean assignmentExists(long groupId, long subjectId) throws ServiceException {
        try {
            return assignmentDao.selectCountByGroupSubject(groupId, subjectId) > 0;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean assignmentExists(long groupId, long teacherId, long subjectId) throws ServiceException {
        try {
            return assignmentDao.selectCountByGroupTeacherSubject(groupId, teacherId, subjectId) > 0;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long create(Assignment assignment) throws ServiceException {
        try {
            return assignmentDao.insert(assignment);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Assignment assignment) throws ServiceException {
        try {
            assignmentDao.update(assignment);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            assignmentDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
