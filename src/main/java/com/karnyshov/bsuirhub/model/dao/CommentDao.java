package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Comment;

import java.util.List;

public interface CommentDao extends BaseDao<Comment> {
    void selectByGrade(int offset, int limit, long gradeId, List<Comment> result) throws DaoException;
    void selectCountByGrade(long gradeId) throws DaoException;
}
