package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.StudyAssignmentDao;
import com.karnyshov.bsuirhub.model.dao.executor.QueryExecutor;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.StudyAssignment;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Named
public class StudyAssignmentDaoImpl implements StudyAssignmentDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String SELECT_BY_ID
            = "SELECT id, id_teacher, id_subject, id_group " +
              "FROM study_assignments " +
              "WHERE id = ?";

    private static final String SELECT_BY_GROUP
            = "SELECT id, id_teacher, id_subject, id_group " +
              "FROM study_assignments " +
              "WHERE id_group = ?";

    private static final String INSERT
            = "INSERT study_assignments (id_teacher, id_subject, id_group) VALUES (?, ?, ?);";

    private static final String UPDATE
            = "UPDATE study_assignments " +
              "SET id_teacher = ?, id_subject = ?, id_group = ? " +
              "WHERE id = ?";

    private static final String DELETE
            = "DELETE FROM study_assignments " +
              "WHERE id = ?;";

    @Inject
    private ResultSetMapper<StudyAssignment> relationMapper;

    @Override
    public void selectAll(int offset, int limit, List<StudyAssignment> result) throws DaoException {
        logger.error("Implementation of StudyAssignmentDao does not support selectAll operation");
        throw new UnsupportedOperationException("Implementation of StudyAssignmentDao does not support selectAll operation");
    }

    @Override
    public long selectTotalCount() throws DaoException {
        logger.error("Implementation of StudyAssignmentDao does not support selectTotalCount operation");
        throw new UnsupportedOperationException("Implementation of StudyAssignmentDao does not support selectTotalCount operation");

    }

    @Override
    public Optional<StudyAssignment> selectById(long id) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(relationMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByGroup(long groupId, List<StudyAssignment> result) throws DaoException {
        QueryExecutor.executeSelect(relationMapper, SELECT_BY_GROUP, result, groupId);
    }

    @Override
    public long insert(StudyAssignment relation) throws DaoException {
        return QueryExecutor.executeInsert(
                INSERT,
                relation.getTeacherId(),
                relation.getSubjectId(),
                relation.getGroupId()
        );
    }

    @Override
    public void update(StudyAssignment relation) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(
                UPDATE,
                relation.getTeacherId(),
                relation.getSubjectId(),
                relation.getGroupId(),
                relation.getEntityId()
        );
    }

    @Override
    public void delete(long id) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE, id);
    }
}
