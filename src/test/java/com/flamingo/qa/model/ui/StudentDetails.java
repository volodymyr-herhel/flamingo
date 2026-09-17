package com.flamingo.qa.model.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/** Test data for the DemoQA "Student Registration" practice form. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetails {
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private List<String> subjects;
    private List<Hobby> hobbies;
    private String currentAddress;
    private String state;
    private String city;

    public String fullName() {
        return firstName + " " + lastName;
    }
}
