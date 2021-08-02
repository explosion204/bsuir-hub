package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.GroupDao;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.GroupService;
import com.karnyshov.bsuirhub.model.service.criteria.GroupFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import static com.karnyshov.bsuirhub.model.service.criteria.GroupFilterCriteria.NONE;

@Named
public class GroupServiceImpl implements GroupService {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    private GroupDao groupDao;

    @Override
    public Optional<Group> findById(long id) throws ServiceException {
        try {
            return groupDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize, GroupFilterCriteria criteria, String keyword, List<Group> result) throws ServiceException {
        int offset = pageSize * (page - 1);
        long totalGroups;

        try {
            switch (criteria) {
                case NONE:
                    groupDao.selectAll(offset, pageSize, result);
                    totalGroups = groupDao.selectTotalCount();
                    break;
                case NAME:
                    groupDao.selectByName(offset, pageSize, keyword, result);
                    totalGroups = groupDao.selectCountByName(keyword);
                    break;
                case DEPARTMENT:
                    long departmentId = Long.parseLong(keyword);
                    groupDao.selectByDepartment(offset, pageSize, departmentId, result);
                    totalGroups = groupDao.selectCountByDepartment(departmentId);
                    break;
                default:
                    throw new ServiceException("Invalid criteria: " + criteria);
            }

            return totalGroups;
        } catch (DaoException | NumberFormatException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize, List<Group> result) throws ServiceException {
        return filter(page, pageSize, NONE, null, result);
    }

    @Override
    public boolean isNameUnique(String name) {
        Optional<Group> group;

        try {
            group = groupDao.selectByName(name);
        } catch (DaoException e) {
            logger.error("Something went wrong (UserService.isLoginUnique)", e);
            return false;
        }

        return group.isEmpty();
    }

    @Override
    public long create(Group group) throws ServiceException {
        try {
            return groupDao.insert(group);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Group group) throws ServiceException {
        try {
            groupDao.update(group);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            groupDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
