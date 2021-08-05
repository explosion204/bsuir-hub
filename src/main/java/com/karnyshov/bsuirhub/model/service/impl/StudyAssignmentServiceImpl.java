package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.StudyAssignmentDao;
import com.karnyshov.bsuirhub.model.entity.StudyAssignment;
import com.karnyshov.bsuirhub.model.service.StudyAssignmentService;
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
    public void findByGroup(long groupId, List<StudyAssignment> result) throws ServiceException {
        try {
            studyAssignmentDao.selectByGroup(groupId, result);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long findByTeacher(int page, int pageSize, long teacherId, List<StudyAssignment> result) throws ServiceException {
        int offset = pageSize * (page - 1);

        try {
            studyAssignmentDao.selectDistinctByTeacher(offset, pageSize, teacherId, result);
            return studyAssignmentDao.selectDistinctCountByTeacher(teacherId);
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
