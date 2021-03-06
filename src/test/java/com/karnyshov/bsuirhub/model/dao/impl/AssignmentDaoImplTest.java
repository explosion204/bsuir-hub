package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.AssignmentDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.AssignmentMapper;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.IntegerMapper;
import com.karnyshov.bsuirhub.model.entity.Assignment;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PoolMockExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssignmentDaoImplTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT assignments (id_teacher, id_subject, id_group) VALUES (?, ?, ?);";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE assignments;";

    private AssignmentDao assignmentDao = new AssignmentDaoImpl(new AssignmentMapper(), new IntegerMapper());
    private List<Assignment> testSample = new ArrayList<>(SAMPLE_SIZE);

    private Supplier<Long> foreignKeyIdSupplier = () -> ThreadLocalRandom.current().nextLong(1, 10);

    @BeforeAll
    public void setUp() throws SQLException, DatabaseConnectionException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                long teacherId = foreignKeyIdSupplier.get();
                long subjectId = foreignKeyIdSupplier.get();
                long groupId = foreignKeyIdSupplier.get();
                Assignment assignment = (Assignment) Assignment.builder()
                        .setTeacherId(teacherId)
                        .setSubjectId(subjectId)
                        .setGroupId(groupId)
                        .setEntityId(i + 1)
                        .build();
                testSample.add(assignment);

                statement.setLong(1, teacherId);
                statement.setLong(2, subjectId);
                statement.setLong(3, groupId);
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    @Test
    @Order(1)
    public void testSelectById() throws DaoException {
        int assignmentId = ThreadLocalRandom.current().nextInt(1, testSample.size() + 1);
        Assignment expected = testSample.get(assignmentId - 1);
        Assignment actual = assignmentDao.selectById(assignmentId).get();
        assertEquals(actual, expected);
    }

    @Test
    @Order(2)
    public void testSelectByGroup() throws DaoException {
        long groupId = foreignKeyIdSupplier.get();
        List<Assignment> expected = testSample.stream()
                .filter(assignment -> assignment.getGroupId() == groupId)
                .collect(Collectors.toList());
        List<Assignment> actual = new LinkedList<>();
        assignmentDao.selectByGroup(0, SAMPLE_SIZE, groupId, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(3)
    public void testSelectCountByGroup() throws DaoException {
        long groupId = foreignKeyIdSupplier.get();
        long expected = testSample.stream()
                .filter(assignment -> assignment.getGroupId() == groupId)
                .count();
        long actual = assignmentDao.selectCountByGroup(groupId);
        assertEquals(actual, expected);
    }

    @Test
    @Order(4)
    public void testSelectByTeacher() throws DaoException {
        long teacherId = foreignKeyIdSupplier.get();
        List<Assignment> expected = testSample.stream()
                .filter(assignment -> assignment.getTeacherId() == teacherId)
                .collect(Collectors.toList());
        List<Assignment> actual = new LinkedList<>();
        assignmentDao.selectByTeacher(0, SAMPLE_SIZE, teacherId, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(5)
    public void testSelectCountByTeacher() throws DaoException {
        long teacherId = foreignKeyIdSupplier.get();
        long expected = testSample.stream()
                .filter(assignment -> assignment.getTeacherId() == teacherId)
                .count();
        long actual = assignmentDao.selectCountByTeacher(teacherId);
        assertEquals(actual, expected);
    }

    @Test
    @Order(6)
    public void testSelectCountByGroupTeacherSubject() throws DaoException {
        long groupId = foreignKeyIdSupplier.get();
        long teacherId = foreignKeyIdSupplier.get();
        long subjectId = foreignKeyIdSupplier.get();
        long expected = testSample.stream()
                .filter(assignment -> assignment.getGroupId() == groupId && assignment.getTeacherId() == teacherId
                        && assignment.getSubjectId() == subjectId)
                .count();
        long actual = assignmentDao.selectCountByGroupTeacherSubject(groupId, teacherId, subjectId);
        assertEquals(actual, expected);
    }

    @Test
    @Order(7)
    public void testInsert() throws DaoException {
        long teacherId = foreignKeyIdSupplier.get();
        long subjectId = foreignKeyIdSupplier.get();
        long groupId = foreignKeyIdSupplier.get();
        long expectedId = testSample.size() + 1;
        Assignment assignment = (Assignment) Assignment.builder()
                .setTeacherId(teacherId)
                .setSubjectId(subjectId)
                .setGroupId(groupId)
                .setEntityId(expectedId)
                .build();
        testSample.add(assignment);

        long actualId = assignmentDao.insert(assignment);
        assertEquals(actualId, expectedId);
    }

    @Test
    @Order(8)
    public void testUpdate() throws DaoException {
        Assignment assignment = testSample.get(testSample.size() - 1);
        long groupId = foreignKeyIdSupplier.get();
        Assignment updatedAssignment = Assignment.builder()
                .of(assignment)
                .setGroupId(groupId)
                .build();

        int expectedRowsAffected = 1;
        int actualRowsAffected = assignmentDao.update(updatedAssignment);
        assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @Test
    @Order(9)
    public void testDelete() throws DaoException {
        Assignment assignment = testSample.remove(testSample.size() - 1);
        long entityId = assignment.getEntityId();

        int expectedRowsAffected = 1;
        int actualRowsAffected = assignmentDao.delete(entityId);
        assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @AfterAll
    public void tearDown() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(TRUNCATE_TABLE);
        }
    }
}
