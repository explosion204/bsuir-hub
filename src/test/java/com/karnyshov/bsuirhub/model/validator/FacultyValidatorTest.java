package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Faculty;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FacultyValidatorTest {
    @DataProvider(name = "faculty-provider")
    public Object[][] facultyProvider() {
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

        return new Object[][] {
                { false, firstInvalidFaculty },
                { false, secondInvalidFaculty },
                { true, validFaculty }
        };
    }

    @Test(dataProvider = "faculty-provider")
    public void testValidateFaculty(boolean expectedResult, Faculty faculty) {
        boolean actualResult = FacultyValidator.validateFaculty(faculty);
        Assert.assertEquals(actualResult, expectedResult);
    }
}
