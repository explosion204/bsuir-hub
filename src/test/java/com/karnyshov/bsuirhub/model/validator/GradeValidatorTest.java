package com.karnyshov.bsuirhub.model.validator;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GradeValidatorTest {
    @ParameterizedTest
    @MethodSource("provideGradeValue")
    public void testValidateGradeValue(boolean expectedResult, byte gradeValue) {
        boolean actualResult = GradeValidator.validateGradeValue(gradeValue);
        assertEquals(actualResult, expectedResult);
    }

    private Stream<Arguments> provideGradeValue() {
        return Stream.of(
                Arguments.of(false, (byte) -1),
                Arguments.of(false, (byte) 11),
                Arguments.of(true, (byte) 9)
        );
    }
}
