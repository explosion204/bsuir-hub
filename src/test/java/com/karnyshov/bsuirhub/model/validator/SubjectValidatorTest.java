package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Subject;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubjectValidatorTest {
    @ParameterizedTest
    @MethodSource("provideSubject")
    public void testValidateSubject(boolean expectedResult, Subject subject) {
        boolean actualResult = SubjectValidator.validateSubject(subject);
        assertEquals(actualResult, expectedResult);
    }

    private Stream<Arguments> provideSubject() {
        Subject firstInvalidSubject = Subject.builder()
                .setName("_3e-e")
                .setShortName("ff2@dfrfijjfrjejfieroifjir4oifoi a")
                .build();

        Subject secondInvalidSubject = Subject.builder()
                .of(firstInvalidSubject)
                .setName("name name name")
                .build();

        Subject validSubject = Subject.builder()
                .of(secondInvalidSubject)
                .setShortName("NAME")
                .build();

        return Stream.of(
                Arguments.of(false, firstInvalidSubject),
                Arguments.of(false, secondInvalidSubject),
                Arguments.of(true, validSubject)
        );
    }
}
