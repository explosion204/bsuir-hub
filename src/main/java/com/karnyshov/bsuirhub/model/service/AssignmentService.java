package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Assignment;
import com.karnyshov.bsuirhub.model.service.criteria.StudyAssignmentFilterCriteria;

import java.util.List;
import java.util.Optional;

public interface AssignmentService {
    Optional<Assignment> findById(long id) throws ServiceException;
    long filter(int page, int pageSize, StudyAssignmentFilterCriteria criteria, long filterId,
            List<Assignment> result) throws ServiceException;
    boolean assignmentExists(long groupId, long subjectId) throws ServiceException;
    boolean assignmentExists(long groupId, long teacherId, long subjectId) throws ServiceException;
    long create(Assignment assignment) throws ServiceException;
    void update(Assignment assignment) throws ServiceException;
    void delete(long id) throws ServiceException;
}
