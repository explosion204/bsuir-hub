package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.GradeDao;
import com.karnyshov.bsuirhub.model.entity.Grade;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Named
public class GradeDaoImpl implements GradeDao {
    private static final String SELECT_BY_ID
            = "SELECT id, value, id_teacher, id_student, id_subject, date, is_exam " +
              "FROM grades " +
              "WHERE id = ?;";

    private static final String SELECT_BY_STUDENT_AND_SUBJECT
            = "SELECT id, value, id_teacher, id_student, id_subject, date, is_exam " +
              "FROM grades " +
              "WHERE id_student = ? AND id_subject = ?;";

    private static final String SELECT_COUNT_BY_STUDENT_AND_SUBJECT
            = "SELECT COUNT(id) " +
              "FROM grades " +
              "WHERE id_student = ? AND id_subject = ?;";

    private static final String INSERT
            = "INSERT grades (value, id_teacher, id_student, id_subject, date, is_exam) VALUES (?, ?, ?, ?, ?, ?);";

    private static final String UPDATE
            = "UPDATE grades " +
              "SET value = ?, id_teacher = ?, id_student = ?, id_subject = ?, date = ?, is_exam = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "DELETE FROM grades " +
              "WHERE id = ?;";

    @Override
    public void selectAll(int offset, int limit, List<Grade> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectTotalCount() throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public Optional<Grade> selectById(long id) throws DaoException {
        return Optional.empty(); // TODO: 7/29/2021  
    }

    @Override
    public void selectByStudentAndSubject(int offset, int limit, long userId, long subjectId, List<Grade> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectCountByStudentAndSubject(long userId, long subjectId) throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public long insert(Grade entity) throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public void update(Grade entity) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public void delete(long id) throws DaoException {
        // TODO: 7/29/2021  
    }
}
