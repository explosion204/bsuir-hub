package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.GradeDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Grade;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * {@code GradeDaoImpl} is an implementation of {@link GradeDao} interfaces.
 * @author Dmitry Karnyshov
 */
public class GradeDaoImpl implements GradeDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String SELECT_BY_ID
            = "SELECT id, value, id_teacher, id_student, id_subject, date " +
              "FROM grades " +
              "WHERE id = ?;";

    private static final String SELECT_BY_STUDENT_AND_SUBJECT
            = "SELECT id, value, id_teacher, id_student, id_subject, date " +
              "FROM grades " +
              "WHERE id_student = ? AND id_subject = ? " +
              "ORDER BY date DESC " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_STUDENT_AND_SUBJECT
            = "SELECT COUNT(id) " +
              "FROM grades " +
              "WHERE id_student = ? AND id_subject = ?;";

    private static final String SELECT_AVERAGE_BY_SUBJECT
            = "SELECT ROUND(AVG(value), 2) " +
              "FROM grades " +
              "WHERE id_student = ? AND id_subject = ?;";

    private static final String SELECT_AVERAGE
            = "SELECT ROUND(AVG(value), 2) " +
              "FROM grades " +
              "WHERE id_student = ?;";

    private static final String INSERT
            = "INSERT grades (value, id_teacher, id_student, id_subject, date) VALUES (?, ?, ?, ?, ?);";

    private static final String UPDATE
            = "UPDATE grades " +
              "SET value = ?, id_teacher = ?, id_student = ?, id_subject = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "DELETE FROM grades " +
              "WHERE id = ?;";

    private static final String DELETE_BY_STUDENT
            = "DELETE FROM grades " +
              "WHERE id_student = ?;";

    private static final String DELETE_COMMENTS_BY_GRADE
            = "DELETE FROM comments " +
            "WHERE id_grade = ?";

    private static final String DELETE_COMMENTS_BY_STUDENT
            = "DELETE FROM comments " +
              "WHERE id_grade IN (SELECT id FROM grades WHERE id_student = ?);";

    private ResultSetMapper<Grade> gradeMapper;
    private ResultSetMapper<Integer> integerMapper;
    private ResultSetMapper<Double> doubleMapper;

    /**
     * Instantiate a new instance of {@code GradeDaoImpl}.
     *
     * @param gradeMapper mapper for grades.
     * @param integerMapper mapper for {@code int} values.
     * @param doubleMapper mapper for {@code double} values.
     */
    @Inject
    public GradeDaoImpl(ResultSetMapper<Grade> gradeMapper, ResultSetMapper<Integer> integerMapper,
                ResultSetMapper<Double> doubleMapper) {
        this.gradeMapper = gradeMapper;
        this.integerMapper = integerMapper;
        this.doubleMapper = doubleMapper;
    }

    @Override
    public void selectAll(int offset, int limit, List<Grade> result) throws DaoException {
        logger.error("Implementation of GradeDao does not support selectAll operation");
        throw new UnsupportedOperationException("Implementation of GradeDao does not support selectAll operation");
    }

    @Override
    public int selectTotalCount() throws DaoException {
        logger.error("Implementation of GradeDao does not support selectTotalCount operation");
        throw new UnsupportedOperationException("Implementation of GradeDao does not support selectTotalCount operation");
    }

    @Override
    public Optional<Grade> selectById(long id) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeSelectForSingleResult(gradeMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByStudentAndSubject(int offset, int limit, long studentId, long subjectId, List<Grade> result)
                throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        queryContext.executeSelect(gradeMapper, SELECT_BY_STUDENT_AND_SUBJECT, result, studentId, subjectId, limit,
                offset);
    }

    @Override
    public int selectCountByStudentAndSubject(long studentId, long subjectId) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Integer> result = queryContext.executeSelectForSingleResult(integerMapper, SELECT_COUNT_BY_STUDENT_AND_SUBJECT,
                studentId, subjectId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_STUDENT_AND_SUBJECT query"));
    }

    @Override
    public double selectAverage(long studentId) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Double> result = queryContext.executeSelectForSingleResult(doubleMapper, SELECT_AVERAGE,
                studentId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_AVERAGE_NOT_EXAM query"));
    }

    @Override
    public double selectAverageBySubject(long studentId, long subjectId) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        Optional<Double> result = queryContext.executeSelectForSingleResult(doubleMapper, SELECT_AVERAGE_BY_SUBJECT,
                studentId, subjectId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_AVERAGE_NOT_EXAM_BY_SUBJECT query"));
    }

    @Override
    public long insert(Grade grade) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeInsert(
                INSERT,
                grade.getValue(),
                grade.getTeacherId(),
                grade.getStudentId(),
                grade.getSubjectId(),
                grade.getDate()
        );
    }

    @Override
    public int update(Grade grade) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(false);
        return queryContext.executeUpdateOrDelete(
                UPDATE,
                grade.getValue(),
                grade.getTeacherId(),
                grade.getStudentId(),
                grade.getSubjectId(),
                grade.getEntityId()
        );
    }

    @Override
    public int delete(long id) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(true);
        int recordsAffected = queryContext.executeUpdateOrDelete(DELETE_COMMENTS_BY_GRADE, id);
        recordsAffected += queryContext.executeUpdateOrDelete(DELETE, id);
        queryContext.commit();
        return recordsAffected;
    }

    @Override
    public int deleteByStudent(long studentId) throws DaoException {
        QueryContext queryContext = QueryContext.createContext(true);
        // delete all comments associated with user (including comments of teachers)
        int recordsAffected = queryContext.executeUpdateOrDelete(DELETE_COMMENTS_BY_STUDENT, studentId);
        recordsAffected +=  queryContext.executeUpdateOrDelete(DELETE_BY_STUDENT, studentId);
        queryContext.commit();
        return recordsAffected;
    }
}
