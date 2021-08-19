package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.AssignmentDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;
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
    private static final Logger logger = LogManager.getLogger();

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
    private ResultSetMapper<Integer> integerMapper;

    /**
     * Instantiate a new instance of {@code AssignmentDaoImpl}.
     *
     * @param assignmentMapper mapper for assignments.
     * @param integerMapper mapper for {@code int} values.
     */
    @Inject
    public AssignmentDaoImpl(ResultSetMapper<Assignment> assignmentMapper, ResultSetMapper<Integer> integerMapper) {
        this.assignmentMapper = assignmentMapper;
        this.integerMapper = integerMapper;
    }

    @Override
    public void selectAll(int offset, int limit, List<Assignment> result) throws DaoException {
        logger.error("Implementation of AssignmentDao does not support selectAll operation");
        throw new UnsupportedOperationException();
    }

    @Override
    public int selectTotalCount() throws DaoException {
        logger.error("Implementation of AssignmentDao does not support selectTotalCount operation");
        throw new UnsupportedOperationException();

    }

    @Override
    public Optional<Assignment> selectById(long id) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeSelectForSingleResult(assignmentMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByGroup(int offset, int limit, long groupId, List<Assignment> result) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        queryContext.executeSelect(assignmentMapper, SELECT_BY_GROUP, result, groupId, limit, offset);
    }

    @Override
    public int selectCountByGroup(long groupId) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Integer> result = queryContext.executeSelectForSingleResult(integerMapper, SELECT_COUNT_BY_GROUP, groupId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_GROUP query"));
    }

    @Override
    public void selectByTeacher(int offset, int limit, long teacherId, List<Assignment> result)
            throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        queryContext.executeSelect(assignmentMapper, SELECT_BY_TEACHER, result, teacherId, limit, offset);
    }

    @Override
    public int selectCountByTeacher(long teacherId) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Integer> result = queryContext.executeSelectForSingleResult(integerMapper, SELECT_COUNT_BY_TEACHER,
                teacherId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_TEACHER query"));
    }

    @Override
    public int selectCountByGroupSubject(long groupId, long subjectId) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Integer> result = queryContext.executeSelectForSingleResult(integerMapper, SELECT_COUNT_BY_GROUP_SUBJECT,
                groupId, subjectId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_GROUP_SUBJECT query"));
    }

    @Override
    public int selectCountByGroupTeacherSubject(long groupId, long teacherId, long subjectId) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Integer> result = queryContext.executeSelectForSingleResult(integerMapper, SELECT_COUNT_BY_GROUP_TEACHER_SUBJECT,
                groupId, teacherId, subjectId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_GROUP_TEACHER_SUBJECT query"));
    }

    @Override
    public long insert(Assignment relation) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeInsert(
                INSERT,
                relation.getTeacherId(),
                relation.getSubjectId(),
                relation.getGroupId()
        );
    }

    @Override
    public int update(Assignment relation) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeUpdateOrDelete(
                UPDATE,
                relation.getTeacherId(),
                relation.getSubjectId(),
                relation.getGroupId(),
                relation.getEntityId()
        );
    }

    @Override
    public int delete(long id) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeUpdateOrDelete(DELETE, id);
    }
}
