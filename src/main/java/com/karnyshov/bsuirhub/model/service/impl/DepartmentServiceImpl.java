package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.DepartmentDao;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.service.DepartmentService;
import com.karnyshov.bsuirhub.model.service.criteria.DepartmentFilterCriteria;
import jakarta.inject.Inject;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Optional;

import static com.karnyshov.bsuirhub.model.service.criteria.DepartmentFilterCriteria.NONE;

/**
 * {@code DepartmentServiceImpl} class is an implementation of {@link DepartmentService} interface.
 * @author Dmitry Karnyshov
 */
public class DepartmentServiceImpl implements DepartmentService {
    private DepartmentDao departmentDao;

    @Inject
    public DepartmentServiceImpl(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public Optional<Department> findById(long id) throws ServiceException {
        try {
            return departmentDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int filter(int start, int size, DepartmentFilterCriteria criteria, String keyword,
                       List<Department> result) throws ServiceException {
        int totalDepartments;

        try {
            switch (criteria) {
                case NONE:
                    departmentDao.selectAll(start, size, result);
                    totalDepartments = departmentDao.selectTotalCount();
                    break;
                case NAME:
                    departmentDao.selectByName(start, size, keyword, result);
                    totalDepartments = departmentDao.selectCountByName(keyword);
                    break;
                case SHORT_NAME:
                    departmentDao.selectByShortName(start, size, keyword, result);
                    totalDepartments = departmentDao.selectCountByShortName(keyword);
                    break;
                case FACULTY:
                    long facultyId = NumberUtils.isParsable(keyword) ? Long.parseLong(keyword) : 0;
                    departmentDao.selectByFaculty(start, size, facultyId, result);
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
    public int filter(int start, int size, List<Department> result) throws ServiceException {
        return filter(start, size, NONE, null, result);
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
