package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserValidatorTest {
    @ParameterizedTest
    @MethodSource("provideUserData")
    public void testValidateUser(boolean expectedResult, User user, String password, String confirmPassword,
                boolean skipRoleValidation, boolean skipEmailValidation, boolean skipPasswordValidation) {
        boolean actualResult = UserValidator.validateUser(user, password, confirmPassword, skipRoleValidation,
                skipEmailValidation, skipPasswordValidation);
        assertEquals(actualResult, expectedResult);
    }

    @ParameterizedTest
    @MethodSource("provideImagePath")
    public void validateImage(boolean expectedResult, String imagePath) {
        boolean actualResult = UserValidator.validateProfileImage(imagePath);
        assertEquals(actualResult, expectedResult);
    }

    private Stream<Arguments> provideUserData() {
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

        return Stream.of(
                // expectedResult, user, password, confirmPassword, skipRoleValidation, skipEmailValidation, skipPasswordValidation
                Arguments.of(false, adminUser, StringUtils.EMPTY, StringUtils.EMPTY, false, true, true),
                Arguments.of(true, adminUser, StringUtils.EMPTY, StringUtils.EMPTY, true, true, true),
                Arguments.of(false, invalidLoginUser, StringUtils.EMPTY, StringUtils.EMPTY, true, true, true),
                Arguments.of(false, invalidEmailUser, StringUtils.EMPTY, StringUtils.EMPTY, true, false, true),
                Arguments.of(true, invalidEmailUser, StringUtils.EMPTY, StringUtils.EMPTY, true, true, true),
                Arguments.of(false, baseUser, "password", "password", true, true, false),
                Arguments.of(false, baseUser, "password1", "password", true, true, false),
                Arguments.of(true, baseUser, "password1", "password1", true, true, false),
                Arguments.of(false, invalidFirstNameUser, "password1", "password1", true, true, false),
                Arguments.of(false, invalidPatronymicUser, "password1", "password1", true, true, false),
                Arguments.of(false, invalidLastNameUser, "password1", "password1", true, true, false),
                Arguments.of(true, baseUser, "password1", "password1", false, false, false)
        );
    }

    private Stream<Arguments> provideImagePath() {
        return Stream.of(
                Arguments.of(true, getResourcePath("data/valid_1.jpg")),
                Arguments.of(true, getResourcePath("data/valid_2.jpg")),
                Arguments.of(true, getResourcePath("data/valid_3.png")),
                Arguments.of(false, getResourcePath("data/invalid.png"))
        );
    }

    private String getResourcePath(String resourceName) {
        ClassLoader loader = UserValidatorTest.class.getClassLoader();
        return loader.getResource(resourceName).getFile();
    }

}
