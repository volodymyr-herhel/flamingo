package com.flamingo.qa.ui.pages;

import com.flamingo.qa.model.ui.PersonRecord;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/** Wraps the Add/Edit "Registration Form" modal on the Web Tables page. */
public class RegistrationFormModal extends BasePage {

    public RegistrationFormModal(Page page) {
        super(page);
    }

    @Step("Fill registration form")
    public RegistrationFormModal fill(PersonRecord person) {
        byId("firstName").fill(person.getFirstName());
        byId("lastName").fill(person.getLastName());
        byId("userEmail").fill(person.getEmail());
        byId("age").fill(String.valueOf(person.getAge()));
        byId("salary").fill(String.valueOf(person.getSalary()));
        byId("department").fill(person.getDepartment());
        return this;
    }

    @Step("Submit registration form")
    public void submit() {
        byId("submit").click();
    }
}
