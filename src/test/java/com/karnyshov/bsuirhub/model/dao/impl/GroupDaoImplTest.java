package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.GroupDao;
import com.karnyshov.bsuirhub.model.dao.mapper.impl.GroupMapper;
import com.karnyshov.bsuirhub.model.dao.mapper.impl.LongMapper;
import com.karnyshov.bsuirhub.model.entity.Group;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GroupDaoImplTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT `groups` (name, id_department, id_headman, id_curator, is_archived) VALUES (?, ?, ?, ?, 0);";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE `groups`;";

    private GroupDao groupDao = new GroupDaoImpl(new GroupMapper(), new LongMapper());
    private List<Group> testSample = new ArrayList<>();

    private Supplier<String> randomStringSupplier = () -> RandomStringUtils.random(10, true, true);
    private Supplier<Integer> foreignKeyIdSupplier = () -> ThreadLocalRandom.current().nextInt(1, 10);

    @BeforeClass
    public void setUp() throws DatabaseConnectionException, SQLException {
        DatabaseMockUtil.mockDatabaseConnectionPool();
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                String randomString = randomStringSupplier.get();
                int departmentId = foreignKeyIdSupplier.get();
                int headmanId = foreignKeyIdSupplier.get();
                int curatorId = foreignKeyIdSupplier.get();
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

    @DataProvider(name = "keyword-provider")
    public Object[][] keywordProvider() {
        Supplier<String> supplier = () -> RandomStringUtils.random(1, true, true);
        return new Object[][] {
                { supplier.get() }, { supplier.get() }, { supplier.get() }, { supplier.get() }, { supplier.get() },
        };
    }

    @Test(groups = "select")
    public void testSelectAll() throws DaoException {
        List<Group> expected = testSample.stream()
                .filter(subject -> !subject.isArchived())
                .collect(Collectors.toList());
        List<Group> actual = new ArrayList<>();
        groupDao.selectAll(0, SAMPLE_SIZE, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectTotalCount() throws DaoException {
        long expected = testSample.stream()
                .filter(subject -> !subject.isArchived())
                .count();
        long actual = groupDao.selectTotalCount();
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectById() throws DaoException {
        int groupId = ThreadLocalRandom.current().nextInt(1, testSample.size() + 1);
        Group expected = testSample.get(groupId - 1);
        Group actual = groupDao.selectById(groupId).get();
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider", groups = "select")
    public void testSelectByName(String keyword) throws DaoException {
        List<Group> expected = testSample.stream()
                .filter(subject -> StringUtils.containsIgnoreCase(subject.getName(), keyword)
                        && !subject.isArchived())
                .collect(Collectors.toList());
        List<Group> actual = new ArrayList<>();
        groupDao.selectByName(0, SAMPLE_SIZE, keyword, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "keyword-provider", groups = "select")
    public void testSelectCountByName(String keyword) throws DaoException {
        long expected = testSample.stream()
                .filter(subject -> StringUtils.containsIgnoreCase(subject.getName(), keyword)
                        && !subject.isArchived())
                .count();
        long actual = groupDao.selectCountByName(keyword);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectByDepartment() throws DaoException {
        int departmentId = foreignKeyIdSupplier.get();
        List<Group> expected = testSample.stream()
                .filter(group -> group.getDepartmentId() == departmentId
                        && !group.isArchived())
                .collect(Collectors.toList());
        List<Group> actual = new ArrayList<>();
        groupDao.selectByDepartment(0, SAMPLE_SIZE, departmentId, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectCountByDepartment() throws DaoException {
        int departmentId = foreignKeyIdSupplier.get();
        long expected = testSample.stream()
                .filter(group -> group.getDepartmentId() == departmentId
                        && !group.isArchived())
                .count();
        long actual = groupDao.selectCountByDepartment(departmentId);
        Assert.assertEquals(actual, expected);
    }

    @AfterClass
    public void tierDown() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(TRUNCATE_TABLE);
        }
    }
}
