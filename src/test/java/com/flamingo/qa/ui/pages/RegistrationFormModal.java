package com.flamingo.qa.ui.pages;

import com.flamingo.qa.model.ui.PersonRecord;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

/** Wraps the Add/Edit "Registration Form" modal on the Web Tables page. */
public class RegistrationFormModal extends BasePage {

    private final Locator firstNameInput = byId("firstName");
    private final Locator lastNameInput = byId("lastName");
    private final Locator emailInput = byId("userEmail");
    private final Locator ageInput = byId("age");
    private final Locator salaryInput = byId("salary");
    private final Locator departmentInput = byId("department");
    private final Locator submitButton = byId("submit");
    private final Locator modalContainer = byId("registration-form-modal");

    public RegistrationFormModal(Page page) {
        super(page);
    }

    public boolean isVisible() {
        return modalContainer.isVisible();
    }

    @Step("Fill registration form")
    public RegistrationFormModal fill(PersonRecord person) {
        firstNameInput.fill(person.getFirstName());
        lastNameInput.fill(person.getLastName());
        emailInput.fill(person.getEmail());
        ageInput.fill(String.valueOf(person.getAge()));
        salaryInput.fill(String.valueOf(person.getSalary()));
        departmentInput.fill(person.getDepartment());
        return this;
    }

    @Step("Submit registration form")
    public RegistrationFormModal submit() {
        submitButton.click();
        return this;
    }
}
