package com.karnyshov.bsuirhub.model.validator;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GradeValidatorTest {
    @DataProvider(name = "grade-value-provider")
    public Object[][] gradeValueProvider() {
        return new Object[][] {
                { false, (byte) -1 },
                { false, (byte) 11 },
                { true, (byte) 9 }
        };
    }

    @Test(dataProvider = "grade-value-provider")
    public void testValidateGradeValue(boolean expectedResult, byte gradeValue) {
        boolean actualResult = GradeValidator.validateGradeValue(gradeValue);
        Assert.assertEquals(actualResult, expectedResult);
    }
}
