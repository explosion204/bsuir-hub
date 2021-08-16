package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Department;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DepartmentValidatorTest {
    @DataProvider(name = "department-provider")
    public Object[][] departmentProvider() {
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

        return new Object[][] {
                { false, firstInvalidDepartment },
                { false, secondInvalidDepartment },
                { false, thirdInvalidDepartment },
                { true, validDepartment }
        };
    }

    @Test(dataProvider = "department-provider")
    public void testValidateDepartment(boolean expectedResult, Department department) {
        boolean actualResult = DepartmentValidator.validateDepartment(department);
        Assert.assertEquals(actualResult, expectedResult);
    }
}
