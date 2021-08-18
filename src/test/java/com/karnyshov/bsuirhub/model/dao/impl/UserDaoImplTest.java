package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.dao.mapper.impl.LongMapper;
import com.karnyshov.bsuirhub.model.dao.mapper.impl.UserMapper;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class UserDaoImplTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT users (login, email, password_hash, salt, id_role, id_status, id_group, first_name, patronymic, " +
            "last_name, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE users;";

    private UserDao userDao = new UserDaoImpl(new UserMapper(), new LongMapper());
    private List<User> testSample = new ArrayList<>();

    private Supplier<String> randomStringSupplier = () -> RandomStringUtils.random(10, true, true);
    private Supplier<Integer> roleIdSupplier = () -> ThreadLocalRandom.current().nextInt(1, 4);
    private Supplier<Integer> statusIdSupplier = roleIdSupplier;
    private Supplier<Integer> groupIdSupplier = () -> ThreadLocalRandom.current().nextInt(1, 10);

    @BeforeClass
    public void setUp() throws DatabaseConnectionException, SQLException {
        DatabaseMockUtil.mockDatabaseConnectionPool();
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

    @DataProvider(name = "keyword-provider")
    public Object[][] keywordProvider() {
        Supplier<String> supplier = () -> RandomStringUtils.random(1, true, true);
        return new Object[][] {
                { supplier.get() }, { supplier.get() }, { supplier.get() }, { supplier.get() }, { supplier.get() },
        };
    }

    @DataProvider(name = "role-id-provider")
    public Object[][] roleIdProvider() {
        return new Object[][] {{ roleIdSupplier.get() }, { roleIdSupplier.get() }, { roleIdSupplier.get() }};
    }

    @DataProvider(name = "group-id-provider")
    public Object[][] groupIdProvider() {
        return new Object[][] {{ groupIdSupplier.get() }, { groupIdSupplier.get() }, { groupIdSupplier.get() }};
    }

    @Test(groups = "select")
    public void testSelectAll() throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectAll(0, SAMPLE_SIZE, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectTotalCount() throws DaoException {
        long expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectTotalCount();
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectById() throws DaoException {
        int userId = ThreadLocalRandom.current().nextInt(1, testSample.size() + 1);
        User expected = testSample.get(userId - 1);
        User actual = userDao.selectById(userId).get();
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectByLoginSingle() throws DaoException {
        Optional<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .findAny();
        Optional<User> actual = userDao.selectByLogin(expected.get().getLogin());
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider", groups = "select")
    public void testSelectByLoginMultiple(String keyword) throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLogin(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectByLogin(0, SAMPLE_SIZE, keyword, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider", groups = "select")
    public void testSelectCountByLoginMultiple(String keyword) throws DaoException {
        long expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLogin(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectCountByLogin(keyword);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectByEmailSingle() throws DaoException {
        Optional<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .findAny();
        Optional<User> actual = userDao.selectByEmail(expected.get().getEmail());
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider", groups = "select")
    public void testSelectByEmailMultiple(String keyword) throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getEmail(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectByEmail(0, SAMPLE_SIZE, keyword, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider", groups = "select")
    public void testSelectCountByEmailMultiple(String keyword) throws DaoException {
        long expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getEmail(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectCountByLogin(keyword);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider", groups = "select")
    public void testSelectByLastName(String keyword) throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLastName(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectByLastName(0, SAMPLE_SIZE, keyword, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider", groups = "select")
    public void testSelectCountByLastName(String keyword) throws DaoException {
        long expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLastName(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectCountByLastName(keyword);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "role-id-provider", groups = "select")
    public void testSelectByRole(int roleId) throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED
                        && user.getRole() == UserRole.parseRole(roleId))
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectByRole(0, SAMPLE_SIZE, roleId, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "role-id-provider", groups = "select")
    public void testSelectCountByRole(int roleId) throws DaoException {
        long expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED
                        && user.getRole() == UserRole.parseRole(roleId))
                .count();
        long actual = userDao.selectCountByRole(roleId);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "group-id-provider", groups = "select")
    public void testSelectByGroup(long groupId) throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED && user.getRole() == UserRole.STUDENT
                        && user.getGroupId() == groupId)
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectByGroup(0, SAMPLE_SIZE, groupId, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "group-id-provider", groups = "select")
    public void testSelectCountByGroup(long groupId) throws DaoException {
        long expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED && user.getRole() == UserRole.STUDENT
                        && user.getGroupId() == groupId)
                .count();
        long actual = userDao.selectCountByGroup(groupId);
        Assert.assertEquals(actual, expected);
    }

    @Test(dependsOnGroups = "select")
    public void testInsert() throws DaoException {
        String randomString = randomStringSupplier.get();
        int roleId = roleIdSupplier.get();
        long statusId = statusIdSupplier.get();
        long entityId = testSample.size() + 1;

        User expectedUser = (User) User.builder()
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
                .setEntityId(entityId)
                .build();
        testSample.add(expectedUser);

        userDao.insert(expectedUser);
        User actualUser = userDao.selectById(entityId).get();
        Assert.assertEquals(actualUser, expectedUser);
    }

    @Test(dependsOnMethods = "testInsert")
    public void testUpdate() throws DaoException {
        User user = testSample.get(testSample.size() - 1);
        long entityId = user.getEntityId();
        User expectedUpdatedUser = User.builder()
                .of(user)
                .setFirstName(randomStringSupplier.get())
                .build();

        userDao.update(expectedUpdatedUser);
        User actualUpdatedUser = userDao.selectById(entityId).get();
        Assert.assertEquals(actualUpdatedUser, expectedUpdatedUser);
    }

    @Test(dependsOnMethods = "testUpdate")
    public void testDelete() throws DaoException {
        User user = testSample.remove(testSample.size() - 1);
        long entityId = user.getEntityId();

        userDao.delete(entityId);
        User deletedUser = userDao.selectById(entityId).get();
        Assert.assertEquals(deletedUser.getStatus(), UserStatus.DELETED);
    }

    @AfterClass
    public void tierDown() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(TRUNCATE_TABLE);
        }
    }
}
