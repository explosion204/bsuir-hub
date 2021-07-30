package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.service.criteria.DepartmentFilterCriteria;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Optional<Department> findById(long id) throws ServiceException;
    long filter(int page, int pageSize, DepartmentFilterCriteria criteria, String keyword, List<Department> result)
            throws ServiceException;
    long filter(int page, int pageSize, List<Department> result) throws ServiceException;
    long create(Department department) throws ServiceException;
    void update(Department department) throws ServiceException;
    void delete(long id) throws ServiceException;

}
