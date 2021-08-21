package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.GradeDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.DoubleMapper;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.GradeMapper;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.IntegerMapper;
import com.karnyshov.bsuirhub.model.entity.Comment;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class GradeDaoImplTest {
    private static final int GRADES_SAMPLE_SIZE = 100;
    private static final int COMMENTS_SAMPLE_SIZE = 50;
    private static final String INSERT_GRADE
            = "INSERT grades (value, id_teacher, id_student, id_subject, date) VALUES (?, ?, ?, ?, ?);";
    private static final String INSERT_COMMENT
            = "INSERT comments (id_grade, id_user, text, creation_time) VALUES (?, ?, ?, ?);";
    private static final String TRUNCATE_GRADES_TABLE = "TRUNCATE TABLE grades;";
    private static final String TRUNCATE_COMMENTS_TABLE = "TRUNCATE TABLE comments;";

    private GradeDao gradeDao = new GradeDaoImpl(new GradeMapper(), new IntegerMapper(), new DoubleMapper());
    private List<Grade> gradesTestSample = new ArrayList<>(GRADES_SAMPLE_SIZE);
    private List<Comment> commentsTestSample = new ArrayList<>(COMMENTS_SAMPLE_SIZE);

    private Supplier<Long> foreignKeyIdSupplier = () -> ThreadLocalRandom.current().nextLong(1, 10);
    private Supplier<Byte> gradeValueSupplier = () -> (byte) ThreadLocalRandom.current().nextInt(1, 11);

    @BeforeAll
    public void setUp() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement gradeStatement = connection.prepareStatement(INSERT_GRADE);
             PreparedStatement commentStatement = connection.prepareStatement(INSERT_COMMENT)) {
            for (int i = 0; i < GRADES_SAMPLE_SIZE; i++) {
                long subjectId = foreignKeyIdSupplier.get();
                long teacherId = foreignKeyIdSupplier.get();
                long studentId = foreignKeyIdSupplier.get();
                byte value = gradeValueSupplier.get();
                LocalDate date = LocalDate.now();

                Grade grade = (Grade) Grade.builder()
                        .setSubjectId(subjectId)
                        .setTeacherId(teacherId)
                        .setStudentId(studentId)
                        .setValue(value)
                        .setDate(date)
                        .setEntityId(i + 1)
                        .build();
                gradesTestSample.add(grade);

                gradeStatement.setByte(1, value);
                gradeStatement.setLong(2, teacherId);
                gradeStatement.setLong(3, studentId);
                gradeStatement.setLong(4, subjectId);
                gradeStatement.setDate(5, Date.valueOf(date));

                gradeStatement.addBatch();
            }

            for (int i = 0; i < COMMENTS_SAMPLE_SIZE; i++) {
                LocalDateTime creationTime = LocalDateTime.now();
                long gradeId = foreignKeyIdSupplier.get();
                long userId = foreignKeyIdSupplier.get();
                String text = "text";

                Comment comment = (Comment) Comment.builder()
                        .setCreationTime(creationTime)
                        .setGradeId(gradeId)
                        .setUserId(userId)
                        .setText(text)
                        .setEntityId(i + 1)
                        .build();
                commentsTestSample.add(comment);

                commentStatement.setLong(1, gradeId);
                commentStatement.setLong(2, userId);
                commentStatement.setString(3, text);
                commentStatement.setTimestamp(4, Timestamp.valueOf(creationTime));

                commentStatement.addBatch();
            }

            gradeStatement.executeBatch();
            commentStatement.executeBatch();
        }
    }

    @Test
    @Order(1)
    public void testSelectById() throws DaoException {
        int gradeId = ThreadLocalRandom.current().nextInt(1, gradesTestSample.size() + 1);
        Grade expected = gradesTestSample.get(gradeId - 1);
        Grade actual = gradeDao.selectById(gradeId).get();
        assertEquals(actual, expected);
    }

    @Test
    @Order(2)
    public void testSelectByStudentAndSubject() throws DaoException {
        long studentId = foreignKeyIdSupplier.get();
        long subjectId = foreignKeyIdSupplier.get();
        List<Grade> expected = gradesTestSample.stream()
                .filter(grade -> grade.getStudentId() == studentId && grade.getSubjectId() == subjectId)
                .collect(Collectors.toList());
        List<Grade> actual = new LinkedList<>();
        gradeDao.selectByStudentAndSubject(0, GRADES_SAMPLE_SIZE, studentId, subjectId, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(3)
    public void testSelectCountByStudentAndSubject() throws DaoException {
        long studentId = foreignKeyIdSupplier.get();
        long subjectId = foreignKeyIdSupplier.get();
        long expected = gradesTestSample.stream()
                .filter(grade -> grade.getStudentId() == studentId && grade.getSubjectId() == subjectId)
                .count();
        int actual = gradeDao.selectCountByStudentAndSubject(studentId, subjectId);
        assertEquals(actual, expected);
    }

    @Test
    @Order(4)
    public void testSelectAverage() throws DaoException {
        long studentId = foreignKeyIdSupplier.get();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        double expected = gradesTestSample.stream()
                .filter(grade -> grade.getStudentId() == studentId)
                .mapToDouble(Grade::getValue)
                .average()
                .orElse(0);
        expected = Double.parseDouble(decimalFormat.format(expected));
        double actual = gradeDao.selectAverage(studentId);
        assertEquals(actual, expected);
    }

    @Test
    @Order(5)
    public void testSelectAverageBySubject() throws DaoException {
        long studentId = foreignKeyIdSupplier.get();
        long subjectId = foreignKeyIdSupplier.get();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        double expected = gradesTestSample.stream()
                .filter(grade -> grade.getStudentId() == studentId && grade.getSubjectId() == subjectId)
                .mapToDouble(Grade::getValue)
                .average()
                .orElse(0);
        expected = Double.parseDouble(decimalFormat.format(expected));
        double actual = gradeDao.selectAverageBySubject(studentId, subjectId);
        assertEquals(actual, expected);
    }

    @Test
    @Order(6)
    public void testInsert() throws DaoException {
        long subjectId = foreignKeyIdSupplier.get();
        long teacherId = foreignKeyIdSupplier.get();
        long studentId = foreignKeyIdSupplier.get();
        byte value = gradeValueSupplier.get();
        LocalDate date = LocalDate.now();
        long expectedId = gradesTestSample.size() + 1;

        Grade grade = (Grade) Grade.builder()
                .setSubjectId(subjectId)
                .setTeacherId(teacherId)
                .setStudentId(studentId)
                .setValue(value)
                .setDate(date)
                .setEntityId(expectedId)
                .build();
        gradesTestSample.add(grade);

        long actualId = gradeDao.insert(grade);
        assertEquals(actualId, expectedId);
    }

    @Test
    @Order(7)
    public void testUpdate() throws DaoException {
        Grade grade = gradesTestSample.get(gradesTestSample.size() - 1);
        Grade updatedGrade = Grade.builder()
                .of(grade)
                .setValue(gradeValueSupplier.get())
                .build();

        int expectedRowsAffected = 1;
        int actualRowsAffected = gradeDao.update(updatedGrade);
        assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @Test
    @Order(8)
    public void testDelete() throws DaoException {
        Grade grade = gradesTestSample.remove(0);
        long entityId = grade.getEntityId();
        long expectedRowsAffected = commentsTestSample.stream()
                .filter(comment -> comment.getGradeId() == entityId)
                .count() + 1;
        int actualRowsAffected = gradeDao.delete(entityId);
        assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @Test
    @Order(9)
    public void testDeleteByStudent() throws DaoException {
        long studentId = foreignKeyIdSupplier.get();
        List<Long> gradeIdsToDelete = new LinkedList<>();
        long expectedGradesCount = gradesTestSample.stream()
                .filter(grade -> grade.getStudentId() == studentId)
                .peek(grade -> gradeIdsToDelete.add(grade.getEntityId()))
                .count();
        long expectedCommentsCount = commentsTestSample.stream()
                .filter(comment -> gradeIdsToDelete.contains(comment.getGradeId()))
                .count();
        long expectedRowsAffected = expectedGradesCount + expectedCommentsCount;
        int actualRowsAffected = gradeDao.deleteByStudent(studentId);
        assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @AfterAll
    public void tearDown() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(TRUNCATE_GRADES_TABLE);
            statement.execute(TRUNCATE_COMMENTS_TABLE);
        }
    }
}
