package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.FacultyDao;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.service.FacultyService;
import com.karnyshov.bsuirhub.model.service.criteria.FacultyFilterCriteria;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

import static com.karnyshov.bsuirhub.model.service.criteria.FacultyFilterCriteria.NONE;

@Named
public class FacultyServiceImpl implements FacultyService {
    @Inject
    private FacultyDao facultyDao;

    @Override
    public Optional<Faculty> findById(long id) throws ServiceException {
        try {
            return facultyDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize, FacultyFilterCriteria criteria, String keyword, List<Faculty> result)
                throws ServiceException {
        int offset = pageSize * (page - 1);
        long totalFaculties;

        try {
            switch (criteria) {
                case NONE:
                    facultyDao.selectAll(offset, pageSize, result);
                    totalFaculties = facultyDao.selectTotalCount();
                    break;
                case NAME:
                    facultyDao.selectByName(offset, pageSize, keyword, result);
                    totalFaculties = facultyDao.selectCountByName(keyword);
                    break;
                case SHORT_NAME:
                    facultyDao.selectByShortName(offset, pageSize, keyword, result);
                    totalFaculties = facultyDao.selectCountByShortName(keyword);
                    break;
                default:
                    throw new ServiceException("Invalid criteria: " + criteria);
            }

            return totalFaculties;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long filter(int page, int pageSize, List<Faculty> result) throws ServiceException {
        return filter(page, pageSize, NONE, null, result);
    }

    @Override
    public long create(Faculty faculty) throws ServiceException {
        try {
            return facultyDao.insert(faculty);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Faculty faculty) throws ServiceException {
        try {
            facultyDao.update(faculty);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            facultyDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
