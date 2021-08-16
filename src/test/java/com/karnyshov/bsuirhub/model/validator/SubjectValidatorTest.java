package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Subject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SubjectValidatorTest {
    @DataProvider(name = "subject-provider")
    public Object[][] subjectProvider() {
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

        return new Object[][] {
                { false, firstInvalidSubject },
                { false, secondInvalidSubject },
                { true, validSubject }
        };
    }

    @Test(dataProvider = "subject-provider")
    public void testValidateSubject(boolean expectedResult, Subject subject) {
        boolean actualResult = SubjectValidator.validateSubject(subject);
        Assert.assertEquals(actualResult, expectedResult);
    }
}
