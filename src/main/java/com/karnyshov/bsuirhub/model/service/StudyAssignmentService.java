package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.StudyAssignment;

import java.util.List;
import java.util.Optional;

public interface StudyAssignmentService {
    Optional<StudyAssignment> findById(long id) throws ServiceException;
    void findByGroup(long groupId, List<StudyAssignment> result) throws ServiceException;
    long findByTeacher(int page, int pageSize, long teacherId, List<StudyAssignment> result) throws ServiceException;
    long create(StudyAssignment studyAssignment) throws ServiceException;
    void update(StudyAssignment studyAssignment) throws ServiceException;
    void delete(long id) throws ServiceException;
}
