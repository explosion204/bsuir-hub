package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.service.criteria.GroupFilterCriteria;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Optional<Group> findById(long id) throws ServiceException;
    long filter(int start, int size, GroupFilterCriteria criteria, String keyword, List<Group> result)
            throws ServiceException;
    long filter(int start, int size, List<Group> result) throws ServiceException;
    boolean isNameUnique(String name);
    long create(Group group) throws ServiceException;
    void update(Group group) throws ServiceException;
    void delete(long id) throws ServiceException;
}
