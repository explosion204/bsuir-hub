package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.GroupTeacherSubjectDao;
import com.karnyshov.bsuirhub.model.entity.GroupTeacherSubject;
import com.karnyshov.bsuirhub.model.service.GroupTeacherSubjectService;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class GroupTeacherSubjectServiceImpl implements GroupTeacherSubjectService {
    @Inject
    private GroupTeacherSubjectDao entityDao;

    @Override
    public Optional<GroupTeacherSubject> findById(long id) throws ServiceException {
        try {
            return entityDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void findByGroup(long groupId, List<GroupTeacherSubject> result) throws ServiceException {
        try {
            entityDao.selectByGroup(groupId, result);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long create(GroupTeacherSubject entity) throws ServiceException {
        try {
            return entityDao.insert(entity);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(GroupTeacherSubject entity) throws ServiceException {
        try {
            entityDao.update(entity);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            entityDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
