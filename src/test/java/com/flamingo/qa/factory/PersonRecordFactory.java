package com.flamingo.qa.factory;

import com.flamingo.qa.model.ui.PersonRecord;
import com.flamingo.qa.util.RandomDataUtils;

/** Produces ready-to-use {@link PersonRecord} test data for the DemoQA Web Tables page. */
public final class PersonRecordFactory {

    private PersonRecordFactory() {
    }

    public static PersonRecord randomPerson() {
        String firstName = RandomDataUtils.randomFirstName();
        String lastName = RandomDataUtils.randomLastName();
        return PersonRecord.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(RandomDataUtils.randomEmail(firstName, lastName))
                .age(RandomDataUtils.randomInt(18, 65))
                .salary(RandomDataUtils.randomInt(1000, 200_000))
                .department("Automation")
                .build();
    }
}
