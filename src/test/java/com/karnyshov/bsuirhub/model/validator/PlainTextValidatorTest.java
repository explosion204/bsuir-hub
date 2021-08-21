package com.karnyshov.bsuirhub.model.validator;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlainTextValidatorTest {
    @ParameterizedTest
    @MethodSource("provideText")
    public void validateText(boolean expectedResult, String text) {
        boolean actualResult = PlainTextValidator.validateText(text);
        assertEquals(actualResult, expectedResult);
    }

    private Stream<Arguments> provideText() {
        return Stream.of(
                Arguments.of(false, ""),
                Arguments.of(true, "text"),
                Arguments.of(false, "9SqFPACDi124yrMFFfOuF777kirf8JJGWGP9Bb4VnkivsDGGDbh1EH1OTd9CUWPs3U6tVny57jgmldMHVEBuf1" +
                        "JY4xY4joLIaRpRWd6MbQldQ7zrGLeMAgtcp7nEFaV5kYL3PUxTiOMsXdsqihyqPupHgKzjvKjcrlzwn3JH89wtj" +
                        "FEMmQIOyT3oYXNKC8YnQTHBMQAWOeRTUIy5E7tFb6chw74I0CrCSI6hnJOQ0iXZRXWFycMFHtRwqemWP9Z4d")
        );
    }
}
