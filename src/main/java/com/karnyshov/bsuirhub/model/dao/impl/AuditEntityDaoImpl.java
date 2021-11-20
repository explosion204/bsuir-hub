package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.AuditEntityDao;
import com.karnyshov.bsuirhub.model.entity.AuditEntity;

import java.util.List;
import java.util.Optional;

public class AuditEntityDaoImpl implements AuditEntityDao {
    private static final String INSERT =
            "INSERT audit (id_user, request, timestamp) VALUES (?, ?, ?);";

    @Override
    public void selectAll(int offset, int limit, List<AuditEntity> result) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int selectTotalCount() throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<AuditEntity> selectById(long id) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long insert(AuditEntity entity) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeInsert(
                INSERT,
                entity.getUserId(),
                entity.getRequest(),
                entity.getTimestamp()
        );
    }

    @Override
    public int update(AuditEntity entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(long id) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
