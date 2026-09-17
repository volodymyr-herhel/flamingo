package com.flamingo.qa.model.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Test data for the DemoQA "Web Tables" registration record. */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PersonRecord {
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private int salary;
    private String department;
}
