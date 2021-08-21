package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.DepartmentDao;
import com.karnyshov.bsuirhub.model.dao.FacultyDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.DepartmentMapper;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.FacultyMapper;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.IntegerMapper;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.entity.Faculty;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

@Test(suiteName = "dao-tests")
public class DepartmentDaoImplTest extends AbstractDaoTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT departments (name, short_name, id_faculty, specialty_alias, is_archived) VALUES (?, ?, ?, ?, 0);";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE departments;";

    private DepartmentDao departmentDao = new DepartmentDaoImpl(new DepartmentMapper(), new IntegerMapper());
    private List<Department> testSample = new ArrayList<>(SAMPLE_SIZE);

    private Supplier<String> randomStringSupplier = () -> RandomStringUtils.random(10, true, true);
    private Supplier<String> keywordSupplier = () -> RandomStringUtils.random(1, true, true);
    private Supplier<Long> foreignKeyIdSupplier = () -> ThreadLocalRandom.current().nextLong(1, 10);

    @BeforeClass
    public void setUp() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                String name = randomStringSupplier.get();
                String shortName = randomStringSupplier.get();
                String specialtyAlias = randomStringSupplier.get();
                long facultyId = foreignKeyIdSupplier.get();

                Department department = (Department) Department.builder()
                        .setName(name)
                        .setShortName(shortName)
                        .setSpecialtyAlias(specialtyAlias)
                        .setFacultyId(facultyId)
                        .setEntityId(i + 1)
                        .build();
                testSample.add(department);

                statement.setString(1, name);
                statement.setString(2, shortName);
                statement.setLong(3, facultyId);
                statement.setString(4, specialtyAlias);
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    @Test(groups = "select")
    public void testSelectAll() throws DaoException {
        List<Department> expected = testSample.stream()
                .filter(department -> !department.isArchived())
                .collect(Collectors.toList());
        List<Department> actual = new LinkedList<>();
        departmentDao.selectAll(0, SAMPLE_SIZE, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectTotalCount() throws DaoException {
        long expected = testSample.stream()
                .filter(department -> !department.isArchived())
                .count();
        long actual = departmentDao.selectTotalCount();
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectById() throws DaoException {
        int departmentId = ThreadLocalRandom.current().nextInt(1, testSample.size() + 1);
        Department expected = testSample.get(departmentId - 1);
        Department actual = departmentDao.selectById(departmentId).get();
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectByName() throws DaoException {
        String keyword = keywordSupplier.get();
        List<Department> expected = testSample.stream()
                .filter(department -> StringUtils.containsIgnoreCase(department.getName(), keyword)
                        && !department.isArchived())
                .collect(Collectors.toList());
        List<Department> actual = new LinkedList<>();
        departmentDao.selectByName(0, SAMPLE_SIZE, keyword, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectCountByName() throws DaoException {
        String keyword = keywordSupplier.get();
        long expected = testSample.stream()
                .filter(department -> StringUtils.containsIgnoreCase(department.getName(), keyword)
                        && !department.isArchived())
                .count();
        long actual = departmentDao.selectCountByName(keyword);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectByShortName() throws DaoException {
        String keyword = keywordSupplier.get();
        List<Department> expected = testSample.stream()
                .filter(department -> StringUtils.containsIgnoreCase(department.getShortName(), keyword)
                        && !department.isArchived())
                .collect(Collectors.toList());
        List<Department> actual = new LinkedList<>();
        departmentDao.selectByShortName(0, SAMPLE_SIZE, keyword, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectCountByShortName() throws DaoException {
        String keyword = keywordSupplier.get();
        long expected = testSample.stream()
                .filter(department -> StringUtils.containsIgnoreCase(department.getShortName(), keyword)
                        && !department.isArchived())
                .count();
        long actual = departmentDao.selectCountByShortName(keyword);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectByFaculty() throws DaoException {
        long facultyId = foreignKeyIdSupplier.get();
        List<Department> expected = testSample.stream()
                .filter(department -> department.getFacultyId() == facultyId && !department.isArchived())
                .collect(Collectors.toList());
        List<Department> actual = new LinkedList<>();
        departmentDao.selectByFaculty(0, SAMPLE_SIZE, facultyId, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectCountByFaculty() throws DaoException {
        long facultyId = foreignKeyIdSupplier.get();
        long expected = testSample.stream()
                .filter(department -> department.getFacultyId() == facultyId && !department.isArchived())
                .count();
        long actual = departmentDao.selectCountByFaculty(facultyId);
        Assert.assertEquals(actual, expected);
    }

    @Test(dependsOnGroups = "select")
    public void testInsert() throws DaoException {
        String name = randomStringSupplier.get();
        String shortName = randomStringSupplier.get();
        String specialtyAlias = randomStringSupplier.get();
        long facultyId = foreignKeyIdSupplier.get();
        long expectedId = testSample.size() + 1;

        Department department = (Department) Department.builder()
                .setName(name)
                .setShortName(shortName)
                .setSpecialtyAlias(specialtyAlias)
                .setFacultyId(facultyId)
                .setEntityId(expectedId)
                .build();
        testSample.add(department);

        long actualId = departmentDao.insert(department);
        Assert.assertEquals(actualId, expectedId);
    }

    @Test(dependsOnMethods = "testInsert")
    public void testUpdate() throws DaoException {
        Department department = testSample.get(testSample.size() - 1);
        Department updatedDepartment = Department.builder()
                .of(department)
                .setName(randomStringSupplier.get())
                .build();

        int expectedRowsAffected = 1;
        int actualRowsAffected = departmentDao.update(updatedDepartment);
        Assert.assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @Test(dependsOnMethods = "testUpdate")
    public void testDelete() throws DaoException {
        Department department = testSample.remove(testSample.size() - 1);
        long entityId = department.getEntityId();

        int expectedRowsAffected = 1;
        int actualRowsAffected = departmentDao.delete(entityId);
        Assert.assertEquals(actualRowsAffected, expectedRowsAffected);
    }

    @AfterClass
    public void tearDown() throws DatabaseConnectionException, SQLException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(TRUNCATE_TABLE);
        }
    }
}
