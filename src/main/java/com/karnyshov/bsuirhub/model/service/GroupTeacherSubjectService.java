package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.GroupTeacherSubject;

import java.util.List;
import java.util.Optional;

public interface GroupTeacherSubjectService {
    Optional<GroupTeacherSubject> findById(long id) throws ServiceException;
    void findByGroup(long groupId, List<GroupTeacherSubject> result) throws ServiceException;
    long create(GroupTeacherSubject entity) throws ServiceException;
    void update(GroupTeacherSubject entity) throws ServiceException;
    void delete(long id) throws ServiceException;
}
