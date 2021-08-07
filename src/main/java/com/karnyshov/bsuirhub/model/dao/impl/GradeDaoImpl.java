package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.GradeDao;
import com.karnyshov.bsuirhub.model.dao.executor.QueryExecutor;
import com.karnyshov.bsuirhub.model.dao.mapper.ResultSetMapper;
import com.karnyshov.bsuirhub.model.entity.Grade;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Named
public class GradeDaoImpl implements GradeDao {
    private static final Logger logger = LogManager.getLogger();

    private static final String SELECT_BY_ID
            = "SELECT id, value, id_teacher, id_student, id_subject, date, is_exam " +
              "FROM grades " +
              "WHERE id = ?;";

    private static final String SELECT_BY_STUDENT_AND_SUBJECT
            = "SELECT id, value, id_teacher, id_student, id_subject, date, is_exam " +
              "FROM grades " +
              "WHERE id_student = ? AND id_subject = ? " +
              "ORDER BY date " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_STUDENT_AND_SUBJECT
            = "SELECT COUNT(id) " +
              "FROM grades " +
              "WHERE id_student = ? AND id_subject = ?;";

    private static final String SELECT_AVERAGE_NOT_EXAM_BY_SUBJECT
            = "SELECT ROUND(AVG(value), 2) " +
              "FROM grades " +
              "WHERE id_student = ? AND id_subject = ? AND is_exam = 0 AND value BETWEEN 1 AND 10;";

    private static final String SELECT_AVERAGE_NOT_EXAM
            = "SELECT ROUND(AVG(value), 2) " +
              "FROM grades " +
              "WHERE id_student = ? AND is_exam = 0 AND value BETWEEN 1 AND 10;";

    private static final String INSERT
            = "INSERT grades (value, id_teacher, id_student, id_subject, date, is_exam) VALUES (?, ?, ?, ?, ?, ?);";

    private static final String UPDATE
            = "UPDATE grades " +
              "SET value = ?, id_teacher = ?, id_student = ?, id_subject = ?, is_exam = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "DELETE FROM grades " +
              "WHERE id = ?;";

    @Inject
    private ResultSetMapper<Grade> gradeMapper;

    @Inject
    private ResultSetMapper<Long> longMapper;

    @Inject
    private ResultSetMapper<Double> doubleMapper;

    @Override
    public void selectAll(int offset, int limit, List<Grade> result) throws DaoException {
        logger.error("Implementation of GradeDao does not support selectAll operation");
        throw new UnsupportedOperationException("Implementation of GradeDao does not support selectAll operation");
    }

    @Override
    public long selectTotalCount() throws DaoException {
        logger.error("Implementation of GradeDao does not support selectTotalCount operation");
        throw new UnsupportedOperationException("Implementation of GradeDao does not support selectTotalCount operation");
    }

    @Override
    public Optional<Grade> selectById(long id) throws DaoException {
        return QueryExecutor.executeSelectForSingleResult(gradeMapper, SELECT_BY_ID, id);
    }

    @Override
    public void selectByStudentAndSubject(int offset, int limit, long studentId, long subjectId, List<Grade> result)
                throws DaoException {
        QueryExecutor.executeSelect(gradeMapper, SELECT_BY_STUDENT_AND_SUBJECT, result, studentId, subjectId, limit,
                offset);
    }

    @Override
    public long selectCountByStudentAndSubject(long studentId, long subjectId) throws DaoException {
        Optional<Long> result = QueryExecutor.executeSelectForSingleResult(longMapper, SELECT_COUNT_BY_STUDENT_AND_SUBJECT,
                studentId, subjectId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_COUNT_BY_STUDENT_AND_SUBJECT query"));
    }

    @Override
    public double selectAverageNotExam(long studentId) throws DaoException {
        Optional<Double> result = QueryExecutor.executeSelectForSingleResult(doubleMapper, SELECT_AVERAGE_NOT_EXAM,
                studentId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_AVERAGE_NOT_EXAM query"));
    }

    @Override
    public double selectAverageNotExamBySubject(long studentId, long subjectId) throws DaoException {
        Optional<Double> result = QueryExecutor.executeSelectForSingleResult(doubleMapper, SELECT_AVERAGE_NOT_EXAM_BY_SUBJECT,
                studentId, subjectId);
        return result.orElseThrow(() -> new DaoException("Error while executing SELECT_AVERAGE_NOT_EXAM_BY_SUBJECT query"));
    }

    @Override
    public long insert(Grade grade) throws DaoException {
        return QueryExecutor.executeInsert(
                INSERT,
                grade.getValue().ordinal(),
                grade.getTeacherId(),
                grade.getStudentId(),
                grade.getSubjectId(),
                grade.getDate(),
                grade.getIsExam()
        );
    }

    @Override
    public void update(Grade grade) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(
                UPDATE,
                grade.getValue().ordinal(),
                grade.getTeacherId(),
                grade.getStudentId(),
                grade.getSubjectId(),
                grade.getIsExam(),
                grade.getEntityId()
        );
    }

    @Override
    public void delete(long id) throws DaoException {
        QueryExecutor.executeUpdateOrDelete(DELETE, id);
    }
}
