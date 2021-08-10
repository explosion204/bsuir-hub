package com.karnyshov.bsuirhub.model.dao.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Comment;
import jakarta.inject.Named;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;

@Named
public class CommentMapper implements ResultSetMapper<Comment> {
    @Override
    public Comment map(ResultSet resultSet) throws DaoException {
        try {
            return (Comment) Comment.builder()
                    .setGradeId(resultSet.getLong(COMMENT_GRADE_ID))
                    .setUserId(resultSet.getLong(COMMENT_USER_ID))
                    .setText(resultSet.getString(COMMENT_TEXT))
                    .setCreationTime(resultSet.getTimestamp(COMMENT_CREATION_TIME).toLocalDateTime())
                    .setEntityId(resultSet.getLong(COMMENT_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
