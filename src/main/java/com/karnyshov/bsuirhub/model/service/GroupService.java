package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.criteria.GroupFilterCriteria;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Optional<Group> findById(long id) throws ServiceException;
    long filter(int page, int pageSize, GroupFilterCriteria criteria, String keyword, List<Group> result)
            throws ServiceException;
    long filter(int page, int pageSize, List<Group> result) throws ServiceException;
    long create(Group group) throws ServiceException;
    void update(Group group) throws ServiceException;
    void delete(long id) throws ServiceException;
}
