package com.karnyshov.bsuirhub.model.validator;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PlainTextValidatorTest {
    @DataProvider(name = "text-provider")
    public Object[][] textProvider() {
        return new Object[][] {
                { false, "" },
                { true, "text" },
                { false, "9SqFPACDi124yrMFFfOuF777kirf8JJGWGP9Bb4VnkivsDGGDbh1EH1OTd9CUWPs3U6tVny57jgmldMHVEBuf1" +
                          "JY4xY4joLIaRpRWd6MbQldQ7zrGLeMAgtcp7nEFaV5kYL3PUxTiOMsXdsqihyqPupHgKzjvKjcrlzwn3JH89wtj" +
                          "FEMmQIOyT3oYXNKC8YnQTHBMQAWOeRTUIy5E7tFb6chw74I0CrCSI6hnJOQ0iXZRXWFycMFHtRwqemWP9Z4d" }
        };
    }

    @Test(dataProvider = "text-provider")
    public void validateText(boolean expectedResult, String text) {
        boolean actualResult = PlainTextValidator.validateText(text);
        Assert.assertEquals(actualResult, expectedResult);
    }
}
