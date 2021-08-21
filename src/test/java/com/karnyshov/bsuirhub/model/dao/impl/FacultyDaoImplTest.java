package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.FacultyDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.FacultyMapper;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.IntegerMapper;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
public class FacultyDaoImplTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT faculties (name, short_name, is_archived) VALUES (?, ?, 0);";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE faculties;";

    private FacultyDao facultyDao = new FacultyDaoImpl(new FacultyMapper(), new IntegerMapper());
    private List<Faculty> testSample = new ArrayList<>(SAMPLE_SIZE);

    private Supplier<String> randomStringSupplier = () -> RandomStringUtils.random(10, true, true);
    private Supplier<String> keywordSupplier = () -> RandomStringUtils.random(1, true, true);

    @BeforeAll
    public void setUp() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                String name = randomStringSupplier.get();
                String shortName = randomStringSupplier.get();
                Faculty faculty = (Faculty) Faculty.builder()
                        .setName(name)
                        .setShortName(shortName)
                        .setEntityId(i + 1)
                        .build();
                testSample.add(faculty);

                statement.setString(1, name);
                statement.setString(2, shortName);

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    @Test
    @Order(1)
    public void testSelectAll() throws DaoException {
        List<Faculty> expected = testSample.stream()
                .filter(subject -> !subject.isArchived())
                .collect(Collectors.toList());
        List<Faculty> actual = new LinkedList<>();
        facultyDao.selectAll(0, SAMPLE_SIZE, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(2)
    public void testSelectTotalCount() throws DaoException {
        long expected = testSample.stream()
                .filter(subject -> !subject.isArchived())
                .count();
        long actual = facultyDao.selectTotalCount();
        assertEquals(actual, expected);
    }

    @Test
    @Order(3)
    public void testSelectById() throws DaoException {
        int facultyId = ThreadLocalRandom.current().nextInt(1, testSample.size() + 1);
        Faculty expected = testSample.get(facultyId - 1);
        Faculty actual = facultyDao.selectById(facultyId).get();
        assertEquals(actual, expected);
    }

    @Test
    @Order(4)
    public void testSelectByName() throws DaoException {
        String keyword = keywordSupplier.get();
        List<Faculty> expected = testSample.stream()
                .filter(faculty -> StringUtils.containsIgnoreCase(faculty.getName(), keyword)
                        && !faculty.isArchived())
                .collect(Collectors.toList());
        List<Faculty> actual = new LinkedList<>();
        facultyDao.selectByName(0, SAMPLE_SIZE, keyword, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(5)
    public void testSelectCountByName() throws DaoException {
        String keyword = keywordSupplier.get();
        long expected = testSample.stream()
                .filter(faculty -> StringUtils.containsIgnoreCase(faculty.getName(), keyword)
                        && !faculty.isArchived())
                .count();
        long actual = facultyDao.selectCountByName(keyword);
        assertEquals(actual, expected);
    }

    @Test
    @Order(6)
    public void testSelectByShortName() throws DaoException {
        String keyword = keywordSupplier.get();
        List<Faculty> expected = testSample.stream()
                .filter(faculty -> StringUtils.containsIgnoreCase(faculty.getShortName(), keyword)
                        && !faculty.isArchived())
                .collect(Collectors.toList());
        List<Faculty> actual = new LinkedList<>();
        facultyDao.selectByShortName(0, SAMPLE_SIZE, keyword, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(7)
    public void testSelectCountByShortName() throws DaoException {
        String keyword = keywordSupplier.get();
        long expected = testSample.stream()
                .filter(faculty -> StringUtils.containsIgnoreCase(faculty.getShortName(), keyword)
                        && !faculty.isArchived())
                .count();
        long actual = facultyDao.selectCountByShortName(keyword);
        assertEquals(actual, expected);
    }

    @Test
    @Order(8)
    public void testInsert() throws DaoException {
        String name = randomStringSupplier.get();
        String shortName = randomStringSupplier.get();
        long expectedId = testSample.size() + 1;

        Faculty faculty = (Faculty) Faculty.builder()
                .setName(name)
                .setShortName(shortName)
                .setEntityId(expectedId)
                .build();
        testSample.add(faculty);

        long actualId = facultyDao.insert(faculty);
        assertEquals(actualId, expectedId);
    }

    @Test
    @Order(9)
    public void testUpdate() throws DaoException {
        Faculty faculty = testSample.get(testSample.size() - 1);
        Faculty updatedFaculty = Faculty.builder()
                .of(faculty)
                .setName(randomStringSupplier.get())
                .build();

        int expectedRowsAffected = 1;
        int actualRowsAffected = facultyDao.update(updatedFaculty);
        assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @Test
    @Order(10)
    public void testDelete() throws DaoException {
        Faculty faculty = testSample.remove(testSample.size() - 1);
        long entityId = faculty.getEntityId();

        int expectedRowsAffected = 1;
        int actualRowsAffected = facultyDao.delete(entityId);
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
