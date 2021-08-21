package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Faculty;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FacultyValidatorTest {
    @ParameterizedTest
    @MethodSource("provideFaculty")
    public void testValidateFaculty(boolean expectedResult, Faculty faculty) {
        boolean actualResult = FacultyValidator.validateFaculty(faculty);
        assertEquals(actualResult, expectedResult);
    }

    public Stream<Arguments> provideFaculty() {
        Faculty firstInvalidFaculty = Faculty.builder()
                .setName(" aaaaa ")
                .setShortName("ff2@dfrfijjfrjejfieroifjir4oifoi a")
                .build();

        Faculty secondInvalidFaculty = Faculty.builder()
                .of(firstInvalidFaculty)
                .setName("name name name")
                .build();

        Faculty validFaculty = Faculty.builder()
                .of(secondInvalidFaculty)
                .setShortName("NAME")
                .build();

        return Stream.of(
                Arguments.of(false, firstInvalidFaculty),
                Arguments.of(false, secondInvalidFaculty),
                Arguments.of(true, validFaculty)
        );
    }
}
