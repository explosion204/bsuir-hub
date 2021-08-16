package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UserValidatorTest {
    @DataProvider(name = "user-data-provider")
    public Object[][] userDataProvider() {
        User baseUser = User.builder()
                .setLogin("explosion204")
                .setEmail("test@email.com")
                .setFirstName("Ivan")
                .setPatronymic("Ivanovich")
                .setLastName("Ivanov")
                .build();

        User adminUser = User.builder()
                .of(baseUser)
                .setRole(UserRole.ADMIN)
                .build();

        User invalidLoginUser = User.builder()
                .of(baseUser)
                .setLogin("login")
                .build();

        User invalidEmailUser = User.builder()
                .of(baseUser)
                .setEmail("testemail.com")
                .build();

        User invalidFirstNameUser = User.builder()
                .of(baseUser)
                .setFirstName("Ivan1")
                .build();

        User invalidPatronymicUser = User.builder()
                .of(baseUser)
                .setPatronymic("Ivanovich_")
                .build();

        User invalidLastNameUser = User.builder()
                .of(baseUser)
                .setFirstName("Ivanov@")
                .build();

        return new Object[][] {
                // expectedResult, user, password, confirmPassword, skipRoleValidation, skipEmailValidation, skipPasswordValidation
                { false, adminUser, StringUtils.EMPTY, StringUtils.EMPTY, false, true, true },
                { true, adminUser, StringUtils.EMPTY, StringUtils.EMPTY, true, true, true },
                { false, invalidLoginUser, StringUtils.EMPTY, StringUtils.EMPTY, true, true, true },
                { false, invalidEmailUser, StringUtils.EMPTY, StringUtils.EMPTY, true, false, true },
                { true, invalidEmailUser, StringUtils.EMPTY, StringUtils.EMPTY, true, true, true },
                { false, baseUser, "password", "password", true, true, false },
                { false, baseUser, "password1", "password", true, true, false },
                { true, baseUser, "password1", "password1", true, true, false },
                { false, invalidFirstNameUser, "password1", "password1", true, true, false },
                { false, invalidPatronymicUser, "password1", "password1", true, true, false },
                { false, invalidLastNameUser, "password1", "password1", true, true, false },
                { true, baseUser, "password1", "password1", false, false, false }
        };
    }

    @DataProvider(name = "image-path-provider")
    public Object[][] imagePathProvider() {
        return new Object[][] {
                { true, getResourcePath("data/valid_1.jpg") },
                { true, getResourcePath("data/valid_2.jpg") },
                { true, getResourcePath("data/valid_3.png") },
                { false, getResourcePath("data/invalid.png") }
        };
    }

    @Test(dataProvider = "user-data-provider")
    public void testValidateUser(boolean expectedResult, User user, String password, String confirmPassword,
                boolean skipRoleValidation, boolean skipEmailValidation, boolean skipPasswordValidation) {
        boolean actualResult = UserValidator.validateUser(user, password, confirmPassword, skipRoleValidation,
                skipEmailValidation, skipPasswordValidation);
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test(dataProvider = "image-path-provider")
    public void validateImage(boolean expectedResult, String imagePath) {
        boolean actualResult = UserValidator.validateProfileImage(imagePath);
        Assert.assertEquals(actualResult, expectedResult);
    }

    private String getResourcePath(String resourceName) {
        ClassLoader loader = UserValidatorTest.class.getClassLoader();
        return loader.getResource(resourceName).getFile();
    }
}
