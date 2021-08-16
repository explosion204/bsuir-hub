package com.karnyshov.bsuirhub.model.validator;

import com.karnyshov.bsuirhub.model.entity.Group;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GroupValidatorTest {
    @DataProvider(name = "group-provider")
    public Object[][] groupProvider() {
        Group firstInvalidGroup = Group.builder()
                .setName("")
                .build();

        Group secondInvalidGroup = Group.builder()
                .setName("edekdoekdokdaojfhj3irg3pfwfeqpfokpwfedkkedkepkod")
                .build();

        Group validGroup = Group.builder()
                .setName("groupname123")
                .build();

        return new Object[][] {
                { false, firstInvalidGroup },
                { false, secondInvalidGroup },
                { true, validGroup }
        };
    }

    @Test(dataProvider = "group-provider")
    public void testValidateGroup(boolean expectedResult, Group group) {
        boolean actualResult = GroupValidator.validateGroup(group);
        Assert.assertEquals(actualResult, expectedResult);
    }
}
