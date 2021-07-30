package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.dao.GroupDao;
import com.karnyshov.bsuirhub.model.entity.Group;
import jakarta.inject.Named;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;

@Named
public class GroupDaoImpl implements GroupDao {
    private static final String SELECT_ALL
            = "SELECT id, name, id_department, id_headman, id_curator " +
              "FROM groups " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_TOTAL_COUNT
            = "SELECT COUNT(id) " +
              "FROM groups;";

    private static final String SELECT_BY_ID
            = "SELECT id, name, id_department, id_headman, id_curator " +
              "FROM groups " +
              "WHERE id = ?;";

    private static final String SELECT_BY_NAME
            = "SELECT id, name, id_department, id_headman, id_curator " +
              "FROM groups " +
              "WHERE name LIKE CONCAT('%', ?, '%') " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_NAME
            = "SELECT COUNT(id) " +
              "FROM groups " +
              "WHERE name LIKE CONCAT('%', ?, '%');";

    private static final String SELECT_BY_FACULTY
            = "SELECT groups.id, groups.name, id_department, id_headman, id_curator " +
              "FROM groups " +
              "INNER JOIN departments " +
              "ON departments.id = id_department AND departments.id_faculty = ? " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_FACULTY
            = "SELECT COUNT(groups.id) " +
              "FROM groups " +
              "INNER JOIN departments " +
              "ON departments.id = id_department AND departments.id_faculty = ?";

    private static final String SELECT_BY_FACULTY_AND_DEPARTMENT
            = "SELECT groups.id, groups.name, id_department, id_headman, id_curator " +
              "FROM groups " +
              "INNER JOIN departments " +
              "ON departments.id = id_department AND departments.id_faculty = ? " +
              "WHERE id_department = ? " +
              "ORDER BY id " +
              "LIMIT ? " +
              "OFFSET ?;";

    private static final String SELECT_COUNT_BY_FACULTY_AND_DEPARTMENT
            = "SELECT COUNT(groups.id) " +
              "FROM groups " +
              "INNER JOIN departments " +
              "ON departments.id = id_department AND departments.id_faculty = ? " +
              "WHERE id_department = ?;";

    private static final String SELECT_TEACHERS_SUBJECTS_RELATION_BY_ID
            = "SELECT id, id_teacher, id_subject " +
              "FROM groups_teachers_subjects " +
              "WHERE id = ?";

    private static final String SELECT_TEACHERS_SUBJECTS_RELATION_BY_GROUP
            = "SELECT id, id_teacher, id_subject " +
              "FROM groups_teachers_subjects " +
              "WHERE id_group = ?";

    private static final String INSERT_TEACHERS_SUBJECTS_RELATION
            = "INSERT groups_teachers_subjects (id_teacher, id_subject, id_group) VALUES (?, ?, ?);";

    private static final String UPDATE_TEACHERS_SUBJECTS_RELATION
            = "UPDATE groups_teachers_subjects " +
              "SET id_teacher = ?, id_subject = ? " +
              "WHERE id = ?";

    private static final String DELETE_TEACHERS_SUBJECTS_RELATION
            = "DELETE FROM groups_teachers_subjects " +
              "WHERE id = ?;";
    
    private static final String INSERT
            = "INSERT groups (name, id_department, id_headman, id_curator) VALUES (?, ?, ?, ?);";

    private static final String UPDATE
            = "UPDATE groups " +
              "SET name = ?, id_department = ?, id_headman = ?, id_curator = ? " +
              "WHERE id = ?;";

    private static final String DELETE
            = "DELETE FROM groups " +
              "WHERE id = ?;";

    @Override
    public void selectAll(int offset, int limit, List<Group> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectTotalCount() throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public Optional<Group> selectById(long id) throws DaoException {
        return Optional.empty(); // TODO: 7/29/2021  
    }

    @Override
    public void selectByName(int offset, int limit, String keyword, List<Group> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectCountByName(String keyword) throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public void selectByFaculty(int offset, int limit, long facultyId, List<Group> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectCountByFaculty(long facultyId) throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public void selectByFacultyAndDepartment(int offset, int limit, long facultyId, long departmentId, List<Group> result) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public long selectCountByFacultyAndDepartment(long facultyId, long departmentId) {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public void selectTeachersSubjectsRelationByGroup(long groupId, List<Pair<Long, Long>> result) throws DaoException {
        // TODO: 7/29/2021
    }

    @Override
    public Optional<Pair<Long, Long>> selectTeachersAndSubjectRelationById(long relationId) throws DaoException {
        return Optional.empty(); // TODO: 7/29/2021
    }

    @Override
    public void insertTeachersSubjectsRelation(long groupId, long teacherId, long subjectId) throws DaoException {
        // TODO: 7/29/2021
    }

    @Override
    public void updateTeachersSubjectRelation(long relationId, long teacherId, long subjectId) throws DaoException {
        // TODO: 7/29/2021
    }

    @Override
    public void deleteTeachersSubjectRelation(long relationId) throws DaoException {
        // TODO: 7/29/2021
    }

    @Override
    public long insert(Group entity) throws DaoException {
        return 0; // TODO: 7/29/2021  
    }

    @Override
    public void update(Group entity) throws DaoException {
        // TODO: 7/29/2021  
    }

    @Override
    public void delete(long id) throws DaoException {
        // TODO: 7/29/2021
    }
}
