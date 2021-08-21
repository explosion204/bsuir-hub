package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.IntegerMapper;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.UserMapper;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PoolMockExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoImplTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT users (login, email, password_hash, salt, id_role, id_status, id_group, first_name, patronymic, " +
            "last_name, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE users;";

    private UserDao userDao = new UserDaoImpl(new UserMapper(), new IntegerMapper());
    private List<User> testSample = new ArrayList<>(SAMPLE_SIZE);

    private Supplier<String> randomStringSupplier = () -> RandomStringUtils.random(10, true, true);
    private Supplier<String> keywordSupplier = () -> RandomStringUtils.random(1, true, true);
    private Supplier<Integer> roleIdSupplier = () -> ThreadLocalRandom.current().nextInt(1, 4);
    private Supplier<Long> statusIdSupplier = () -> ThreadLocalRandom.current().nextLong(1, 4);
    private Supplier<Long> groupIdSupplier = () -> ThreadLocalRandom.current().nextLong(1, 10);

    @BeforeAll
    public void setUp() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                String randomString = randomStringSupplier.get();
                int roleId = roleIdSupplier.get();
                long statusId = statusIdSupplier.get();
                long groupId = groupIdSupplier.get();

                User user = (User) User.builder()
                        .setLogin(randomString)
                        .setEmail(randomString)
                        .setPasswordHash(randomString)
                        .setSalt(randomString)
                        .setRole(UserRole.parseRole(roleId))
                        .setStatus(UserStatus.parseStatus(statusId))
                        .setGroupId(groupId)
                        .setFirstName(randomString)
                        .setPatronymic(randomString)
                        .setLastName(randomString)
                        .setProfileImageName(randomString)
                        .setEntityId(i + 1)
                        .build();
                testSample.add(user);

                statement.setString(1, randomString);
                statement.setString(2, randomString);
                statement.setString(3, randomString);
                statement.setString(4, randomString);
                statement.setLong(5, roleId);
                statement.setLong(6, statusId);
                statement.setLong(7, groupId); // group
                statement.setString(8, randomString);
                statement.setString(9, randomString);
                statement.setString(10, randomString);
                statement.setString(11, randomString);
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    @Test
    @Order(1)
    public void testSelectAll() throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new LinkedList<>();
        userDao.selectAll(0, SAMPLE_SIZE, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(2)
    public void testSelectTotalCount() throws DaoException {
        long expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectTotalCount();
        assertEquals(actual, expected);
    }

    @Test
    @Order(3)
    public void testSelectById() throws DaoException {
        int userId = ThreadLocalRandom.current().nextInt(1, testSample.size() + 1);
        User expected = testSample.get(userId - 1);
        User actual = userDao.selectById(userId).get();
        assertEquals(actual, expected);
    }

    @Test
    @Order(4)
    public void testSelectByLoginSingle() throws DaoException {
        Optional<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .findAny();
        Optional<User> actual = userDao.selectByLogin(expected.get().getLogin());
        assertEquals(actual, expected);
    }

    @Test
    @Order(5)
    public void testSelectByLoginMultiple() throws DaoException {
        String keyword = keywordSupplier.get();
        List<User> expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLogin(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new LinkedList<>();
        userDao.selectByLogin(0, SAMPLE_SIZE, keyword, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(6)
    public void testSelectCountByLoginMultiple() throws DaoException {
        String keyword = keywordSupplier.get();
        long expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLogin(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectCountByLogin(keyword);
        assertEquals(actual, expected);
    }

    @Test
    @Order(7)
    public void testSelectByEmailSingle() throws DaoException {
        Optional<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .findAny();
        Optional<User> actual = userDao.selectByEmail(expected.get().getEmail());
        assertEquals(actual, expected);
    }

    @Test
    @Order(8)
    public void testSelectByEmailMultiple() throws DaoException {
        String keyword = keywordSupplier.get();
        List<User> expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getEmail(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new LinkedList<>();
        userDao.selectByEmail(0, SAMPLE_SIZE, keyword, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(9)
    public void testSelectCountByEmailMultiple() throws DaoException {
        String keyword = keywordSupplier.get();
        long expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getEmail(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectCountByLogin(keyword);
        assertEquals(actual, expected);
    }

    @Test
    @Order(10)
    public void testSelectByLastName() throws DaoException {
        String keyword = keywordSupplier.get();
        List<User> expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLastName(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new LinkedList<>();
        userDao.selectByLastName(0, SAMPLE_SIZE, keyword, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(11)
    public void testSelectCountByLastName() throws DaoException {
        String keyword = keywordSupplier.get();
        long expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLastName(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectCountByLastName(keyword);
        assertEquals(actual, expected);
    }

    @Test
    @Order(12)
    public void testSelectByRole() throws DaoException {
        int roleId = roleIdSupplier.get();
        List<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED
                        && user.getRole() == UserRole.parseRole(roleId))
                .collect(Collectors.toList());
        List<User> actual = new LinkedList<>();
        userDao.selectByRole(0, SAMPLE_SIZE, roleId, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(13)
    public void testSelectCountByRole() throws DaoException {
        int roleId = roleIdSupplier.get();
        long expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED
                        && user.getRole() == UserRole.parseRole(roleId))
                .count();
        long actual = userDao.selectCountByRole(roleId);
        assertEquals(actual, expected);
    }

    @Test
    @Order(14)
    public void testSelectByGroup() throws DaoException {
        long groupId = groupIdSupplier.get();
        List<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED && user.getRole() == UserRole.STUDENT
                        && user.getGroupId() == groupId)
                .collect(Collectors.toList());
        List<User> actual = new LinkedList<>();
        userDao.selectByGroup(0, SAMPLE_SIZE, groupId, actual);
        assertEquals(actual, expected);
    }

    @Test
    @Order(15)
    public void testSelectCountByGroup() throws DaoException {
        long groupId = groupIdSupplier.get();
        long expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED && user.getRole() == UserRole.STUDENT
                        && user.getGroupId() == groupId)
                .count();
        long actual = userDao.selectCountByGroup(groupId);
        assertEquals(actual, expected);
    }

    @Test
    @Order(16)
    public void testInsert() throws DaoException {
        String randomString = randomStringSupplier.get();
        int roleId = roleIdSupplier.get();
        long statusId = statusIdSupplier.get();
        long expectedId = testSample.size() + 1;

        User user = (User) User.builder()
                .setLogin(randomString)
                .setEmail(randomString)
                .setPasswordHash(randomString)
                .setSalt(randomString)
                .setRole(UserRole.parseRole(roleId))
                .setStatus(UserStatus.parseStatus(statusId))
                .setFirstName(randomString)
                .setPatronymic(randomString)
                .setLastName(randomString)
                .setProfileImageName(randomString)
                .setEntityId(expectedId)
                .build();
        testSample.add(user);

        long actualId = userDao.insert(user);
        assertEquals(actualId, expectedId);
    }

    @Test
    @Order(17)
    public void testUpdate() throws DaoException {
        User user = testSample.get(testSample.size() - 1);
        User updatedUser = User.builder()
                .of(user)
                .setFirstName(randomStringSupplier.get())
                .build();

        int expectedRowsAffected = 1;
        int actualRowsAffected = userDao.update(updatedUser);
        assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @Test
    @Order(18)
    public void testDelete() throws DaoException {
        User user = testSample.remove(testSample.size() - 1);
        long entityId = user.getEntityId();

        int expectedRowsAffected = 1;
        int actualRowsAffected = userDao.delete(entityId);
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
