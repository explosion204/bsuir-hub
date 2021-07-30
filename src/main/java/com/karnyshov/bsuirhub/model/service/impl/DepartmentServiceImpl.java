package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.DepartmentDao;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import com.karnyshov.bsuirhub.model.service.criteria.DepartmentFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

import static com.karnyshov.bsuirhub.model.service.criteria.DepartmentFilterCriteria.NONE;

@Named
public class DepartmentServiceImpl implements DepartmentService {
    @Inject
    private DepartmentDao departmentDao;

    @Override
    public Optional<Department> findById(long id) throws ServiceException {
        try {
            return departmentDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize, DepartmentFilterCriteria criteria, String keyword,
                List<Department> result) throws ServiceException {
        int offset = pageSize * (page - 1);
        long totalDepartments;

        try {
            switch (criteria) {
                case NONE:
                    departmentDao.selectAll(offset, pageSize, result);
                    totalDepartments = departmentDao.selectTotalCount();
                    break;
                case NAME:
                    departmentDao.selectByName(offset, pageSize, keyword, result);
                    totalDepartments = departmentDao.selectCountByName(keyword);
                    break;
                case SHORT_NAME:
                    departmentDao.selectByShortName(offset, pageSize, keyword, result);
                    totalDepartments = departmentDao.selectCountByShortName(keyword);
                    break;
                case FACULTY:
                    long facultyId = Long.parseLong(keyword);
                    departmentDao.selectByFaculty(offset, pageSize, facultyId, result);
                    totalDepartments = departmentDao.selectCountByFaculty(facultyId);
                    break;
                default:
                    throw new ServiceException("Invalid criteria: " + criteria);
            }

            return totalDepartments;
        } catch (DaoException | NumberFormatException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize, List<Department> result) throws ServiceException {
        return filter(page, pageSize, NONE, null, result);
    }

    @Override
    public long create(Department department) throws ServiceException {
        try {
            return departmentDao.insert(department);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Department department) throws ServiceException {
        try {
            departmentDao.update(department);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            departmentDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
