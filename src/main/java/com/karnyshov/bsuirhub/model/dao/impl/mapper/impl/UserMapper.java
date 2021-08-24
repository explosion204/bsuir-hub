package com.karnyshov.bsuirhub.model.dao.impl.mapper.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.entity.UserStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.karnyshov.bsuirhub.model.dao.TableColumn.*;
import static com.karnyshov.bsuirhub.model.dao.TableColumn.USER_ID;

/**
 * {@code UserMapper} is an implementation of {@link ResultSetMapper} interface and provides mapping for
 * {@link User} object.
 * @author Dmitry Karnyshov
 */
public class UserMapper implements ResultSetMapper<User> {
    @Override
    public User map(ResultSet resultSet) throws DaoException {
        try {
            return (User) User.builder()
                    .setLogin(resultSet.getString(USER_LOGIN))
                    .setEmail(resultSet.getString(USER_EMAIL))
                    .setPasswordHash(resultSet.getString(USER_PASSWORD_HASH))
                    .setSalt(resultSet.getString(USER_SALT))
                    .setRole(UserRole.parseRole(resultSet.getInt(USER_ROLE_ID)))
                    .setStatus(UserStatus.parseStatus(resultSet.getLong(USER_STATUS_ID)))
                    .setGroupId(resultSet.getLong(USER_GROUP_ID))
                    .setFirstName(resultSet.getString(USER_FIRST_NAME))
                    .setPatronymic(resultSet.getString(USER_PATRONYMIC))
                    .setLastName(resultSet.getString(USER_LAST_NAME))
                    .setProfileImageName(resultSet.getString(USER_PROFILE_IMAGE))
                    .setEntityId(resultSet.getLong(USER_ID))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
