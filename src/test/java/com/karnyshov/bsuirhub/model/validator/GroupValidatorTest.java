package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Group;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroupValidatorTest {
    @ParameterizedTest
    @MethodSource("provideGroup")
    public void testValidateGroup(boolean expectedResult, Group group) {
        boolean actualResult = GroupValidator.validateGroup(group);
        assertEquals(actualResult, expectedResult);
    }

    private Stream<Arguments> provideGroup() {
        Group firstInvalidGroup = Group.builder()
                .setName("")
                .build();

        Group secondInvalidGroup = Group.builder()
                .setName("edekdoekdokdaojfhj3irg3pfwfeqpfokpwfedkkedkepkod")
                .build();

        Group validGroup = Group.builder()
                .setName("groupname123")
                .build();

        return Stream.of(
                Arguments.of(false, firstInvalidGroup),
                Arguments.of(false, secondInvalidGroup),
                Arguments.of(true, validGroup)
        );
    }
}
