package test.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.dao.impl.UserDaoImpl;
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
import test.karnyshov.bsuirhub.MockUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class UserDaoTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT users (login, email, password_hash, salt, id_role, id_status, id_group, first_name, patronymic, " +
            "last_name, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String DISABLE_FOREIGN_KEY_CHECKS = "SET foreign_key_checks = 0;";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE users;";
    private static final String ENABLE_FOREIGN_KEY_CHECKS = "SET foreign_key_checks = 1;";

    private UserDao userDao = new UserDaoImpl(new UserMapper(), new LongMapper());
    private List<User> testSample = new ArrayList<>();

    private Supplier<String> randomStringSupplier = () -> RandomStringUtils.random(10, true, true);
    private Supplier<Integer> roleIdSupplier = () -> ThreadLocalRandom.current().nextInt(1, 4);
    private Supplier<Integer> statusIdSupplier = roleIdSupplier;
    private Supplier<Long> groupIdSupplier = () -> ThreadLocalRandom.current().nextLong(1, 10);

    @BeforeClass
    public void setUp() throws DatabaseConnectionException, SQLException {
        MockUtil.mockDatabaseConnectionPool();
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.addBatch(DISABLE_FOREIGN_KEY_CHECKS);

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

            statement.addBatch(ENABLE_FOREIGN_KEY_CHECKS);
            statement.executeBatch();
        }
    }

    @DataProvider(name = "keyword-provider")
    public Object[][] keywordProvider() {
        Supplier<String> supplier = () -> RandomStringUtils.random(3, true, true);
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

    @Test
    public void testSelectAll() throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectAll(0, SAMPLE_SIZE, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testSelectTotalCount() throws DaoException {
        long expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectTotalCount();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testSelectById() throws DaoException {
        User expected = testSample.get(15);
        User actual = userDao.selectById(16).get();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testSelectByLoginSingle() throws DaoException {
        Optional<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .findAny();
        Optional<User> actual = userDao.selectByLogin(expected.get().getLogin());
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider")
    public void testSelectByLoginMultiple(String keyword) throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLogin(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectByLogin(0, SAMPLE_SIZE, keyword, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider")
    public void testSelectCountByLoginMultiple(String keyword) throws DaoException {
        long expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLogin(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectCountByLogin(keyword);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testSelectByEmailSingle() throws DaoException {
        Optional<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED)
                .findAny();
        Optional<User> actual = userDao.selectByEmail(expected.get().getEmail());
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider")
    public void testSelectByEmailMultiple(String keyword) throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getEmail(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectByEmail(0, SAMPLE_SIZE, keyword, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider")
    public void testSelectCountByEmailMultiple(String keyword) throws DaoException {
        long expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getEmail(), keyword))
                .count();
        long actual = userDao.selectCountByLogin(keyword);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider")
    public void testSelectByLastName(String keyword) throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLastName(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectByLastName(0, SAMPLE_SIZE, keyword, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider")
    public void testSelectCountByLastName(String keyword) throws DaoException {
        long expected = testSample.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getLastName(), keyword)
                        && user.getStatus() != UserStatus.DELETED)
                .count();
        long actual = userDao.selectCountByLastName(keyword);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "role-id-provider")
    public void testSelectByRole(int roleId) throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED
                        && user.getRole() == UserRole.parseRole(roleId))
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectByRole(0, SAMPLE_SIZE, roleId, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "role-id-provider")
    public void testSelectCountByRole(int roleId) throws DaoException {
        long expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED
                        && user.getRole() == UserRole.parseRole(roleId))
                .count();
        long actual = userDao.selectCountByRole(roleId);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "group-id-provider")
    public void testSelectByGroup(long groupId) throws DaoException {
        List<User> expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED && user.getRole() == UserRole.STUDENT
                        && user.getGroupId() == groupId)
                .collect(Collectors.toList());
        List<User> actual = new ArrayList<>();
        userDao.selectByGroup(0, SAMPLE_SIZE, groupId, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "group-id-provider")
    public void testSelectCountByGroup(long groupId) throws DaoException {
        long expected = testSample.stream()
                .filter(user -> user.getStatus() != UserStatus.DELETED && user.getRole() == UserRole.STUDENT
                        && user.getGroupId() == groupId)
                .count();
        long actual = userDao.selectCountByGroup(groupId);
        Assert.assertEquals(actual, expected);
    }

    @AfterClass
    public void tierDown() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            for (int i = 0; i < 50; i++) {
                statement.addBatch(DISABLE_FOREIGN_KEY_CHECKS);
                statement.addBatch(TRUNCATE_TABLE);
                statement.addBatch(ENABLE_FOREIGN_KEY_CHECKS);
                statement.executeBatch();
            }
        }
    }
}
