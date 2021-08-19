package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.GradeDao;
import com.karnyshov.bsuirhub.model.dao.mapper.impl.DoubleMapper;
import com.karnyshov.bsuirhub.model.dao.mapper.impl.GradeMapper;
import com.karnyshov.bsuirhub.model.dao.mapper.impl.IntegerMapper;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.mockito.MockedStatic;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Test(suiteName = "dao-tests")
public class GradeDaoImplTest extends AbstractDaoTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT grades (value, id_teacher, id_student, id_subject, date) VALUES (?, ?, ?, ?, ?);";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE grades;";

    private GradeDao gradeDao = new GradeDaoImpl(new GradeMapper(), new IntegerMapper(), new DoubleMapper());
    private List<Grade> testSample = new ArrayList<>();

    private Supplier<Long> foreignKeyIdSupplier = () -> ThreadLocalRandom.current().nextLong(1, 10);
    private Supplier<Byte> gradeValueSupplier = () -> (byte) ThreadLocalRandom.current().nextInt(1, 11);

    @BeforeClass
    public void setUp() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (int i = 0; i < SAMPLE_SIZE; i++) {
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
                testSample.add(grade);

                statement.setByte(1, value);
                statement.setLong(2, teacherId);
                statement.setLong(3, studentId);
                statement.setLong(4, subjectId);
                statement.setDate(5, Date.valueOf(date));

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    @Test(groups = "select")
    public void testSelectById() throws DaoException {
        int gradeId = ThreadLocalRandom.current().nextInt(1, testSample.size() + 1);
        Grade expected = testSample.get(gradeId - 1);
        Grade actual = gradeDao.selectById(gradeId).get();
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectByStudentAndSubject() throws DaoException {
        long studentId = foreignKeyIdSupplier.get();
        long subjectId = foreignKeyIdSupplier.get();
        List<Grade> expected = testSample.stream()
                .filter(grade -> grade.getStudentId() == studentId && grade.getSubjectId() == subjectId)
                .collect(Collectors.toList());
        List<Grade> actual = new ArrayList<>();
        gradeDao.selectByStudentAndSubject(0, SAMPLE_SIZE, studentId, subjectId, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectCountByStudentAndSubject() throws DaoException {
        long studentId = foreignKeyIdSupplier.get();
        long subjectId = foreignKeyIdSupplier.get();
        long expected = testSample.stream()
                .filter(grade -> grade.getStudentId() == studentId && grade.getSubjectId() == subjectId)
                .count();
        int actual = gradeDao.selectCountByStudentAndSubject(studentId, subjectId);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectAverage() throws DaoException {
        long studentId = foreignKeyIdSupplier.get();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        double expected = testSample.stream()
                .filter(grade -> grade.getStudentId() == studentId)
                .mapToDouble(Grade::getValue)
                .average()
                .orElse(0);
        expected = Double.parseDouble(decimalFormat.format(expected));
        double actual = gradeDao.selectAverage(studentId);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectAverageBySubject() throws DaoException {
        long studentId = foreignKeyIdSupplier.get();
        long subjectId = foreignKeyIdSupplier.get();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        double expected = testSample.stream()
                .filter(grade -> grade.getStudentId() == studentId && grade.getSubjectId() == subjectId)
                .mapToDouble(Grade::getValue)
                .average()
                .orElse(0);
        expected = Double.parseDouble(decimalFormat.format(expected));
        double actual = gradeDao.selectAverageBySubject(studentId, subjectId);
        Assert.assertEquals(actual, expected);
    }

    @Test(dependsOnGroups = "select")
    public void testInsert() throws DaoException {
        long subjectId = foreignKeyIdSupplier.get();
        long teacherId = foreignKeyIdSupplier.get();
        long studentId = foreignKeyIdSupplier.get();
        byte value = gradeValueSupplier.get();
        LocalDate date = LocalDate.now();
        long entityId = testSample.size() + 1;

        Grade expectedGrade = (Grade) Grade.builder()
                .setSubjectId(subjectId)
                .setTeacherId(teacherId)
                .setStudentId(studentId)
                .setValue(value)
                .setDate(date)
                .setEntityId(entityId)
                .build();
        testSample.add(expectedGrade);

        gradeDao.insert(expectedGrade);
        Grade actualGrade = gradeDao.selectById(entityId).get();
        Assert.assertEquals(actualGrade, expectedGrade);
    }

    @Test(dependsOnMethods = "testInsert")
    public void testUpdate() throws DaoException {
        Grade grade = testSample.get(testSample.size() - 1);
        long entityId = grade.getEntityId();
        Grade expectedUpdatedGrade = Grade.builder()
                .of(grade)
                .setValue(gradeValueSupplier.get())
                .build();

        gradeDao.update(expectedUpdatedGrade);
        Grade actualUpdatedGrade = gradeDao.selectById(entityId).get();
        Assert.assertEquals(actualUpdatedGrade, expectedUpdatedGrade);
    }

    @Test(dependsOnMethods = "testUpdate")
    public void testDelete() throws DaoException {
        Grade grade = testSample.remove(testSample.size() - 1);
        long entityId = grade.getEntityId();

        gradeDao.delete(entityId);
        Optional<Grade> deletedGrade = gradeDao.selectById(entityId);
        Assert.assertTrue(deletedGrade.isEmpty());
    }

    @Test(dependsOnMethods = "testDelete")
    public void testDeleteByStudent() throws DaoException {
        long studentId = foreignKeyIdSupplier.get();
        long expectedCount = testSample.stream()
                .filter(grade -> grade.getStudentId() == studentId)
                .count();
        testSample.removeIf(grade -> grade.getStudentId() == studentId);

        int actualCount = gradeDao.deleteByStudent(studentId);
        Assert.assertEquals(actualCount, expectedCount);
    }

    @AfterClass
    public void tearDown() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(TRUNCATE_TABLE);
        }
    }
}
