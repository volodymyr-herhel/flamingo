package com.flamingo.qa.factory;

import com.flamingo.qa.model.ui.Gender;
import com.flamingo.qa.model.ui.Hobby;
import com.flamingo.qa.model.ui.StudentDetails;
import com.flamingo.qa.util.RandomDataUtils;

import java.time.LocalDate;
import java.util.List;

/** Produces ready-to-use {@link StudentDetails} test data for the DemoQA practice form. */
public final class StudentDetailsFactory {

    private StudentDetailsFactory() {
    }

    public static StudentDetails randomStudent() {
        String firstName = RandomDataUtils.randomFirstName();
        String lastName = RandomDataUtils.randomLastName();
        return StudentDetails.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(RandomDataUtils.randomEmail(firstName, lastName))
                .gender(Gender.FEMALE)
                .mobileNumber("9" + String.format("%09d", RandomDataUtils.randomInt(0, 1_000_000_000)))
                .dateOfBirth(LocalDate.now().minusYears(20))
                .subjects(List.of("Maths"))
                .hobbies(List.of(Hobby.READING))
                .currentAddress("221B Baker Street")
                .state("NCR")
                .city("Delhi")
                .build();
    }
}
