package com.karnyshov.bsuirhub.model.dao.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.DatabaseConnectionException;
import com.karnyshov.bsuirhub.model.dao.CommentDao;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.CommentMapper;
import com.karnyshov.bsuirhub.model.dao.impl.mapper.impl.IntegerMapper;
import com.karnyshov.bsuirhub.model.entity.Comment;
import com.karnyshov.bsuirhub.model.entity.Department;
import com.karnyshov.bsuirhub.model.entity.Grade;
import com.karnyshov.bsuirhub.model.entity.Subject;
import com.karnyshov.bsuirhub.model.pool.DatabaseConnectionPool;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Test(suiteName = "dao-tests")
public class CommentDaoImplTest extends AbstractDaoTest {
    private static final int SAMPLE_SIZE = 100;
    private static final String INSERT
            = "INSERT comments (id_grade, id_user, text, creation_time) VALUES (?, ?, ?, ?);";
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE comments;";

    private CommentDao commentDao = new CommentDaoImpl(new CommentMapper(), new IntegerMapper());
    private List<Comment> testSample = new ArrayList<>(SAMPLE_SIZE);

    private Supplier<Long> foreignKeyIdSupplier = () -> ThreadLocalRandom.current().nextLong(1, 10);

    @BeforeClass
    public void setUp() throws SQLException, DatabaseConnectionException {
        try (Connection connection = DatabaseConnectionPool.getInstance().acquireConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (int i = 0; i < SAMPLE_SIZE; i++) {
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
                testSample.add(comment);

                statement.setLong(1, gradeId);
                statement.setLong(2, userId);
                statement.setString(3, text);
                statement.setTimestamp(4, Timestamp.valueOf(creationTime));

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    @Test(groups = "select")
    public void testSelectById() throws DaoException {
        int commentId = ThreadLocalRandom.current().nextInt(1, testSample.size() + 1);
        Comment expected = testSample.get(commentId - 1);
        Comment actual = commentDao.selectById(commentId).get();
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectByGrade() throws DaoException {
        long gradeId = foreignKeyIdSupplier.get();
        List<Comment> expected = testSample.stream()
                .filter(comment -> comment.getGradeId() == gradeId)
                .collect(Collectors.toList());
        List<Comment> actual = new LinkedList<>();
        commentDao.selectByGrade(0, SAMPLE_SIZE, gradeId, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = "select")
    public void testSelectCountByGrade() throws DaoException {
        long gradeId = foreignKeyIdSupplier.get();
        long expected = testSample.stream()
                .filter(department -> department.getGradeId() == gradeId)
                .count();
        long actual = commentDao.selectCountByGrade(gradeId);
        Assert.assertEquals(actual, expected);
    }

    @Test(dependsOnGroups = "select")
    public void testInsert() throws DaoException {
        LocalDateTime creationTime = LocalDateTime.now();
        long gradeId = foreignKeyIdSupplier.get();
        long userId = foreignKeyIdSupplier.get();
        String text = "text";
        long expectedId = testSample.size() + 1;

        Comment comment = (Comment) Comment.builder()
                .setCreationTime(creationTime)
                .setGradeId(gradeId)
                .setUserId(userId)
                .setText(text)
                .setEntityId(expectedId)
                .build();
        testSample.add(comment);

        long actualId = commentDao.insert(comment);
        Assert.assertEquals(actualId, expectedId);
    }

    @Test(dependsOnMethods = "testInsert")
    public void testDelete() throws DaoException {
        Comment comment = testSample.remove(testSample.size() - 1);
        long entityId = comment.getEntityId();

        int expectedRowsAffected = 1;
        int actualRowsAffected = commentDao.delete(entityId);
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
