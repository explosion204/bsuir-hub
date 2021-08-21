package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Department;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepartmentValidatorTest {
    @ParameterizedTest
    @MethodSource("provideDepartment")
    public void testValidateDepartment(boolean expectedResult, Department department) {
        boolean actualResult = DepartmentValidator.validateDepartment(department);
        assertEquals(actualResult, expectedResult);
    }

    private Stream<Arguments> provideDepartment() {
        Department firstInvalidDepartment = Department.builder()
                .setName("dlpdeldle ")
                .setShortName("d____")
                .setSpecialtyAlias("")
                .build();

        Department secondInvalidDepartment = Department.builder()
                .of(firstInvalidDepartment)
                .setName("validname")
                .build();

        Department thirdInvalidDepartment = Department.builder()
                .of(secondInvalidDepartment)
                .setShortName("SHORT")
                .build();

        Department validDepartment = Department.builder()
                .of(thirdInvalidDepartment)
                .setSpecialtyAlias("just an alias")
                .build();

        return Stream.of(
                Arguments.of(false, firstInvalidDepartment),
                Arguments.of(false, secondInvalidDepartment),
                Arguments.of(false, thirdInvalidDepartment),
                Arguments.of(true, validDepartment)
        );
    }
}
