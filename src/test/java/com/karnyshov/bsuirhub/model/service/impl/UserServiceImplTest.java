package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.UserDao;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserStatus;
import com.karnyshov.bsuirhub.model.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.function.Supplier;

public class UserServiceImplTest {
    private Supplier<String> randomStringSupplier = () -> RandomStringUtils.random(10, true, true);

    @DataProvider(name = "user-provider")
    public Object[][] userProvider() {
        String login = randomStringSupplier.get();
        String password = randomStringSupplier.get();
        String salt = randomStringSupplier.get();
        String passwordHash = DigestUtils.sha256Hex(password + salt);
        User user = User.builder()
                .setLogin(login)
                .setPasswordHash(passwordHash)
                .setSalt(salt)
                .build();

        return new Object[][] {
                { Pair.of(user, password) }
        };
    }

    @Test(dataProvider = "user-provider")
    public void testAuthenticateSuccess(Pair<User, String> userWithPassword)
            throws DaoException, ServiceException {
        User expectedUser = userWithPassword.getLeft();
        String password = userWithPassword.getRight();
        String login = expectedUser.getLogin();
        UserDao userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.selectByLogin(expectedUser.getLogin()))
                .thenReturn(Optional.of(expectedUser));

        UserService userService = new UserServiceImpl(userDao);
        User actualUser = userService.authenticate(login, password).get();

        Assert.assertEquals(actualUser, expectedUser);
    }

    @Test
    public void testAuthenticateInvalidLogin() throws DaoException, ServiceException {
        UserDao userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.selectByLogin(StringUtils.EMPTY))
                .thenReturn(Optional.empty());

        UserService userService = new UserServiceImpl(userDao);
        Optional<User> actualUser = userService.authenticate(StringUtils.EMPTY, StringUtils.EMPTY);

        Assert.assertTrue(actualUser.isEmpty());
    }

    @Test(dataProvider = "user-provider")
    public void testAuthenticateInvalidPassword(Pair<User, String> userWithPassword)
            throws DaoException, ServiceException {
        User expectedUser = userWithPassword.getLeft();
        String login = expectedUser.getLogin();

        UserDao userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.selectByLogin(expectedUser.getLogin()))
                .thenReturn(Optional.of(expectedUser));
        UserService userService = new UserServiceImpl(userDao);

        Optional<User> actualUser = userService.authenticate(login, StringUtils.EMPTY);
        Assert.assertTrue(actualUser.isEmpty());
    }

    @Test(dataProvider = "user-provider")
    public void testChangePassword(Pair<User, String> userWithPassword) throws DaoException, ServiceException {
        User expectedUser = userWithPassword.getLeft();
        long userId = expectedUser.getEntityId();
        String newPassword = randomStringSupplier.get();

        UserDao userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.selectById(userId))
                .thenReturn(Optional.of(expectedUser));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.doNothing().when(userDao).update(userCaptor.capture());

        UserService userService = new UserServiceImpl(userDao);
        userService.changePassword(userId, newPassword);
        User actualUser = userCaptor.getValue();

        Assert.assertNotEquals(actualUser, expectedUser);
    }

    @Test(dataProvider = "user-provider")
    public void testChangeEmail(Pair<User, String> userWithPassword) throws DaoException, ServiceException {
        User expectedUser = userWithPassword.getLeft();
        long userId = expectedUser.getEntityId();
        String newEmail = randomStringSupplier.get();

        UserDao userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.selectById(userId))
                .thenReturn(Optional.of(expectedUser));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.doNothing().when(userDao).update(userCaptor.capture());

        UserService userService = new UserServiceImpl(userDao);
        userService.changeEmail(userId, newEmail);
        User actualUser = userCaptor.getValue();

        Assert.assertTrue(!actualUser.equals(expectedUser)
                && actualUser.getStatus() == UserStatus.NOT_CONFIRMED);
    }

    @Test(dataProvider = "user-provider")
    public void testUpdateUserWithoutPassword(Pair<User, String> userWithPassword)
            throws DaoException, ServiceException {
        User user = userWithPassword.getLeft();
        long userId = user.getEntityId();
        User expectedUpdatedUser = User.builder()
                .of(user)
                .setFirstName(randomStringSupplier.get())
                .build();

        UserDao userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.selectById(userId))
                .thenReturn(Optional.of(user));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.doNothing().when(userDao).update(userCaptor.capture());

        UserService userService = new UserServiceImpl(userDao);
        userService.update(expectedUpdatedUser, StringUtils.EMPTY);
        User actualUpdatedUser = userCaptor.getValue();

        Assert.assertEquals(actualUpdatedUser, expectedUpdatedUser);
    }

    @Test(dataProvider = "user-provider")
    public void testUpdateUserWithPassword(Pair<User, String> userWithPassword)
            throws DaoException, ServiceException {
        User user = userWithPassword.getLeft();
        long userId = user.getEntityId();
        String newPassword = randomStringSupplier.get();

        User expectedUpdatedUser = User.builder()
                .of(user)
                .setFirstName(randomStringSupplier.get())
                .build();

        UserDao userDao = Mockito.mock(UserDao.class);
        Mockito.when(userDao.selectById(userId))
                .thenReturn(Optional.of(user));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.doNothing().when(userDao).update(userCaptor.capture());

        UserService userService = new UserServiceImpl(userDao);
        userService.update(expectedUpdatedUser, newPassword);
        User actualUpdatedUser = userCaptor.getValue();

        Assert.assertNotEquals(actualUpdatedUser, expectedUpdatedUser);
    }
}
