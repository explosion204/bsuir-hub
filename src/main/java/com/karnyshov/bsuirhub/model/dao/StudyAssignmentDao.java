package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.StudyAssignment;

import java.util.List;


public interface StudyAssignmentDao extends BaseDao<StudyAssignment> {
    void selectByGroup(long groupId, List<StudyAssignment> result) throws DaoException;
}
