package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.AssignmentDao;
import com.karnyshov.bsuirhub.model.dao.executor.QueryExecutor;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Assignment;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;


/**
 * {@code AssignmentDaoImpl} is an implementation of {@link AssignmentDao} interfaces.
 * @author Dmitry Karnyshov
 */
@Named
public class AssignmentDaoImpl implements AssignmentDao {
    private static final String SELECT_BY_ID
            = "SELECT id, id_teacher, id_subject, id_group " +
              "FROM assignments " +
              "WHERE id = ?";

    private static final String SELECT_BY_GROUP
            = "SELECT id, id_teacher, id_subject, id_group " +
              "FROM assignments " +
              "WHERE id_group = ? " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_GROUP
            = "SELECT COUNT(id) " +
              "FROM assignments " +
              "WHERE id_group = ?;";

    private static final String SELECT_BY_TEACHER
            = "SELECT id, id_teacher, id_subject, id_group " +
              "FROM assignments " +
              "WHERE id_teacher = ? " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_TEACHER
            = "SELECT COUNT(id) " +
              "FROM assignments " +
              "WHERE id_teacher = ?;";

    private static final String SELECT_COUNT_BY_GROUP_SUBJECT
            = "SELECT COUNT(id) " +
              "FROM assignments " +
              "WHERE id_group = ? AND id_subject = ?;";

    private static final String SELECT_COUNT_BY_GROUP_TEACHER_SUBJECT
            = "SELECT COUNT(id) " +
              "FROM assignments " +
              "WHERE id_group = ? AND id_teacher = ? AND id_subject = ?;";

    private static final String INSERT
            = "INSERT assignments (id_teacher, id_subject, id_group) VALUES (?, ?, ?);";

    private static final String UPDATE
            = "UPDATE assignments " +
              "SET id_teacher = ?, id_subject = ?, id_group = ? " +
              "WHERE id = ?";

    private static final String DELETE
            = "DELETE FROM assignments " +
              "WHERE id = ?;";

    private ResultSetMapper<Assignment> assignmentMapper;
    private ResultSetMapper<Long> longMapper;

    /**
     * Instantiate a new instance of {@code AssignmentDaoImpl}.
     *
     * @param assignmentMapper mapper for assignments.
     * @param longMapper mapper for {@code long} values.
     */
    @Inject
    public AssignmentDaoImpl(ResultSetMapper<Assignment> assignmentMapper, ResultSetMapper<Long> longMapper) {
        this.assignmentMapper = assignmentMapper;
        this.longMapper = longMapper;
    }

    @Override
    public void selectAll(int offset, int limit, List<Assignment> result) throws DaoException {
        throw new UnsupportedOperationException("Implementation of AssignmentDao does not support selectAll operation");
    }

    @Override
    public long selectTotalCount() throws DaoException {
        throw new UnsupportedOperationException("Implementation of AssignmentDao does not support selectTotalCount operation");

    }

    @Override
    public Optional<Assignment> selectById(long id) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(assignmentMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByGroup(int offset, int limit, long groupId, List<Assignment> result) throws DaoException {
        QueryExecutor.executeSelect(assignmentMapper, SELECT_BY_GROUP, result, groupId, limit, offset);
    }

    @Override
    public long selectCountByGroup(long groupId) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_GROUP, groupId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_GROUP query"));
    }

    @Override
    public void selectByTeacher(int offset, int limit, long teacherId, List<Assignment> result)
            throws DaoException {
        QueryExecutor.executeSelect(assignmentMapper, SELECT_BY_TEACHER, result, teacherId, limit, offset);
    }

    @Override
    public long selectCountByTeacher(long teacherId) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_TEACHER,
                teacherId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_TEACHER query"));
    }

    @Override
    public long selectCountByGroupSubject(long groupId, long subjectId) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_GROUP_SUBJECT,
                groupId, subjectId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_GROUP_SUBJECT query"));
    }

    @Override
    public long selectCountByGroupTeacherSubject(long groupId, long teacherId, long subjectId) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_GROUP_TEACHER_SUBJECT,
                groupId, teacherId, subjectId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_GROUP_TEACHER_SUBJECT query"));
    }

    @Override
    public long insert(Assignment relation) throws DaoException {
        return QueryExecutor.executeInsert(
                INSERT,
                relation.getTeacherId(),
                relation.getSubjectId(),
                relation.getGroupId()
        );
    }

    @Override
    public void update(Assignment relation) throws DaoException {
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
