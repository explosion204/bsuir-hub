package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.SubjectDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.IntegerMapper;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.SubjectMapper;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
public class SubjectDaoImplTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT subjects (name, short_name, is_archived) VALUES (?, ?, 0);";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE subjects;";

    private SubjectDao subjectDao = new SubjectDaoImpl(new SubjectMapper(), new IntegerMapper());
    private List<Subject> testSample = new ArrayList<>(SAMPLE_SIZE);

    private Supplier<String> randomStringSupplier = () -> RandomStringUtils.random(10, true, true);
    private Supplier<String> keywordSupplier = () -> RandomStringUtils.random(1, true, true);

    @BeforeAll
    public void setUp() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                String randomString = randomStringSupplier.get();
                Subject subject = (Subject) Subject.builder()
                        .setName(randomString)
                        .setShortName(randomString)
                        .setArchived(false)
                        .setEntityId(i + 1)
                        .build();
                testSample.add(subject);

                statement.setString(1, randomString);
                statement.setString(2, randomString);

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    @Test
    @Order(1)
    public void testSelectAll() throws DaoException {
        List<Subject> expected = testSample.stream()
                .filter(subject -> !subject.isArchived())
                .collect(Collectors.toList());
        List<Subject> actual = new LinkedList<>();
        subjectDao.selectAll(0, SAMPLE_SIZE, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(2)
    public void testSelectTotalCount() throws DaoException {
        long expected = testSample.stream()
                .filter(subject -> !subject.isArchived())
                .count();
        long actual = subjectDao.selectTotalCount();
        assertEquals(actual, expected);
    }

    @Test
    @Order(3)
    public void testSelectById() throws DaoException {
        int subjectId = ThreadLocalRandom.current().nextInt(1, testSample.size() + 1);
        Subject expected = testSample.get(subjectId - 1);
        Subject actual = subjectDao.selectById(subjectId).get();
        assertEquals(actual, expected);
    }

    @Test
    @Order(4)
    public void testSelectByName() throws DaoException {
        String keyword = keywordSupplier.get();
        List<Subject> expected = testSample.stream()
                .filter(subject -> StringUtils.containsIgnoreCase(subject.getName(), keyword)
                        && !subject.isArchived())
                .collect(Collectors.toList());
        List<Subject> actual = new LinkedList<>();
        subjectDao.selectByName(0, SAMPLE_SIZE, keyword, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(5)
    public void testSelectCountByName() throws DaoException {
        String keyword = keywordSupplier.get();
        long expected = testSample.stream()
                .filter(subject -> StringUtils.containsIgnoreCase(subject.getName(), keyword)
                        && !subject.isArchived())
                .count();
        long actual = subjectDao.selectCountByName(keyword);
        assertEquals(actual, expected);
    }

    @Test
    @Order(6)
    public void testSelectByShortName() throws DaoException {
        String keyword = keywordSupplier.get();
        List<Subject> expected = testSample.stream()
                .filter(subject -> StringUtils.containsIgnoreCase(subject.getShortName(), keyword)
                        && !subject.isArchived())
                .collect(Collectors.toList());
        List<Subject> actual = new LinkedList<>();
        subjectDao.selectByShortName(0, SAMPLE_SIZE, keyword, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(7)
    public void testSelectCountByShortName() throws DaoException {
        String keyword = keywordSupplier.get();
        long expected = testSample.stream()
                .filter(subject -> StringUtils.containsIgnoreCase(subject.getShortName(), keyword)
                        && !subject.isArchived())
                .count();
        long actual = subjectDao.selectCountByShortName(keyword);
        assertEquals(actual, expected);
    }

    @Test
    @Order(8)
    public void testInsert() throws DaoException {
        String randomString = randomStringSupplier.get();
        long expectedId = testSample.size() + 1;

        Subject subject = (Subject) Subject.builder()
                .setName(randomString)
                .setShortName(randomString)
                .setArchived(false)
                .setEntityId(expectedId)
                .build();
        testSample.add(subject);

        long actualId = subjectDao.insert(subject);
        assertEquals(actualId, expectedId);
    }

    @Test
    @Order(9)
    public void testUpdate() throws DaoException {
        Subject subject = testSample.get(testSample.size() - 1);
        Subject updatedSubject = Subject.builder()
                .of(subject)
                .setName(randomStringSupplier.get())
                .build();

        int expectedRowsAffected = 1;
        int actualRowsAffected = subjectDao.update(updatedSubject);
        assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @Test
    @Order(10)
    public void testDelete() throws DaoException {
        Subject subject = testSample.remove(testSample.size() - 1);
        long entityId = subject.getEntityId();

        int expectedRowsAffected = 1;
        int actualRowsAffected = subjectDao.delete(entityId);
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
