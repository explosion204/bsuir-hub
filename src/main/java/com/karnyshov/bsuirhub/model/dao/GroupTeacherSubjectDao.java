package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.GroupTeacherSubject;

import java.util.List;


public interface GroupTeacherSubjectDao extends BaseDao<GroupTeacherSubject> {
    void selectByGroup(long groupId, List<GroupTeacherSubject> result) throws DaoException;
}
