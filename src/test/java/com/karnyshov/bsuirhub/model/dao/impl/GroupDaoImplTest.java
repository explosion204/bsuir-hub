package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.GroupDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.GroupMapper;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.IntegerMapper;
import com.karnyshov.bsuirhub.model.entity.Group;
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
public class GroupDaoImplTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT `groups` (name, id_department, id_headman, id_curator, is_archived) VALUES (?, ?, ?, ?, 0);";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE `groups`;";

    private GroupDao groupDao = new GroupDaoImpl(new GroupMapper(), new IntegerMapper());
    private List<Group> testSample = new ArrayList<>(SAMPLE_SIZE);

    private Supplier<String> randomStringSupplier = () -> RandomStringUtils.random(10, true, true);
    private Supplier<String> keywordSupplier = () -> RandomStringUtils.random(1, true, true);
    private Supplier<Long> foreignKeyIdSupplier = () -> ThreadLocalRandom.current().nextLong(1, 10);

    @BeforeAll
    public void setUp() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                String randomString = randomStringSupplier.get();
                long departmentId = foreignKeyIdSupplier.get();
                long headmanId = foreignKeyIdSupplier.get();
                long curatorId = foreignKeyIdSupplier.get();
                Group group = (Group) Group.builder()
                        .setName(randomString)
                        .setHeadmanId(headmanId)
                        .setCuratorId(curatorId)
                        .setDepartmentId(departmentId)
                        .setEntityId(i + 1)
                        .build();

                testSample.add(group);

                statement.setString(1, randomString);
                statement.setLong(2, departmentId);
                statement.setLong(3, headmanId);
                statement.setLong(4, curatorId);

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    @Test
    @Order(1)
    public void testSelectAll() throws DaoException {
        List<Group> expected = testSample.stream()
                .filter(subject -> !subject.isArchived())
                .collect(Collectors.toList());
        List<Group> actual = new LinkedList<>();
        groupDao.selectAll(0, SAMPLE_SIZE, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(2)
    public void testSelectTotalCount() throws DaoException {
        long expected = testSample.stream()
                .filter(subject -> !subject.isArchived())
                .count();
        long actual = groupDao.selectTotalCount();
        assertEquals(actual, expected);
    }

    @Test
    @Order(3)
    public void testSelectById() throws DaoException {
        int groupId = ThreadLocalRandom.current().nextInt(1, testSample.size() + 1);
        Group expected = testSample.get(groupId - 1);
        Group actual = groupDao.selectById(groupId).get();
        assertEquals(actual, expected);
    }

    @Test
    @Order(4)
    public void testSelectByName() throws DaoException {
        String keyword = keywordSupplier.get();
        List<Group> expected = testSample.stream()
                .filter(subject -> StringUtils.containsIgnoreCase(subject.getName(), keyword)
                        && !subject.isArchived())
                .collect(Collectors.toList());
        List<Group> actual = new LinkedList<>();
        groupDao.selectByName(0, SAMPLE_SIZE, keyword, actual);
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
        long actual = groupDao.selectCountByName(keyword);
        assertEquals(actual, expected);
    }

    @Test
    @Order(6)
    public void testSelectByDepartment() throws DaoException {
        long departmentId = foreignKeyIdSupplier.get();
        List<Group> expected = testSample.stream()
                .filter(group -> group.getDepartmentId() == departmentId
                        && !group.isArchived())
                .collect(Collectors.toList());
        List<Group> actual = new LinkedList<>();
        groupDao.selectByDepartment(0, SAMPLE_SIZE, departmentId, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(7)
    public void testSelectCountByDepartment() throws DaoException {
        long departmentId = foreignKeyIdSupplier.get();
        long expected = testSample.stream()
                .filter(group -> group.getDepartmentId() == departmentId
                        && !group.isArchived())
                .count();
        long actual = groupDao.selectCountByDepartment(departmentId);
        assertEquals(actual, expected);
    }

    @Test
    @Order(8)
    public void testInsert() throws DaoException {
        String randomString = randomStringSupplier.get();
        long departmentId = foreignKeyIdSupplier.get();
        long headmanId = foreignKeyIdSupplier.get();
        long curatorId = foreignKeyIdSupplier.get();
        long expectedId = testSample.size() + 1;
        Group group = (Group) Group.builder()
                .setName(randomString)
                .setHeadmanId(headmanId)
                .setCuratorId(curatorId)
                .setDepartmentId(departmentId)
                .setEntityId(expectedId)
                .build();
        testSample.add(group);

        long actualId = groupDao.insert(group);
        assertEquals(actualId, expectedId);
    }

    @Test
    @Order(9)
    public void testUpdate() throws DaoException {
        Group group = testSample.get(testSample.size() - 1);
        Group updatedGroup = Group.builder()
                .of(group)
                .setName(randomStringSupplier.get())
                .build();

        int expectedRowsAffected = 1;
        int actualRowsAffected = groupDao.update(updatedGroup);
        assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @Test
    @Order(10)
    public void testDelete() throws DaoException {
        Group group = testSample.remove(testSample.size() - 1);
        long entityId = group.getEntityId();

        int expectedRowsAffected = 1;
        int actualRowsAffected = groupDao.delete(entityId);
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
