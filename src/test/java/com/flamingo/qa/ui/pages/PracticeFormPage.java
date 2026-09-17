package com.flamingo.qa.ui.pages;

import com.flamingo.qa.config.Config;
import com.flamingo.qa.model.ui.Gender;
import com.flamingo.qa.model.ui.Hobby;
import com.flamingo.qa.model.ui.StudentDetails;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.nio.file.Path;
import java.time.LocalDate;

/** Page object for https://demoqa.com/automation-practice-form. */
public class PracticeFormPage extends BasePage {

    public PracticeFormPage(Page page) {
        super(page);
    }

    @Step("Open the practice form page")
    public PracticeFormPage open() {
        page.navigate(Config.DEMOQA_BASE_URL + "/automation-practice-form");
        disableAdOverlays();
        return this;
    }

    @Step("Fill the student registration form")
    public PracticeFormPage fillStudentDetails(StudentDetails student) {
        byId("firstName").fill(student.getFirstName());
        byId("lastName").fill(student.getLastName());
        byId("userEmail").fill(student.getEmail());
        byId("gender-radio-" + student.getGender().getRadioIndex()).check();
        byId("userNumber").fill(student.getMobileNumber());
        setDateOfBirth(student.getDateOfBirth());
        student.getSubjects().forEach(this::addSubject);
        student.getHobbies().forEach(hobby -> byId("hobbies-checkbox-" + hobby.getCheckboxIndex()).check());
        byId("currentAddress").fill(student.getCurrentAddress());
        selectReactSelectOption("#state", student.getState());
        selectReactSelectOption("#city", student.getCity());
        return this;
    }

    @Step("Upload a picture")
    public PracticeFormPage uploadPicture(Path filePath) {
        byId("uploadPicture").setInputFiles(filePath);
        return this;
    }

    @Step("Set date of birth: {date}")
    public PracticeFormPage setDateOfBirth(LocalDate date) {
        byId("dateOfBirthInput").click();
        page.locator(".react-datepicker__month-select").selectOption(String.valueOf(date.getMonthValue() - 1));
        page.locator(".react-datepicker__year-select").selectOption(String.valueOf(date.getYear()));
        page.locator(".react-datepicker__day--" + String.format("%03d", date.getDayOfMonth())
                + ":not(.react-datepicker__day--outside-month)").click();
        return this;
    }

    @Step("Add subject: {subject}")
    public PracticeFormPage addSubject(String subject) {
        byId("subjectsInput").fill(subject);
        byId("subjectsInput").press("Enter");
        return this;
    }

    @Step("Submit the form")
    public SubmissionModal submit() {
        Locator submitButton = byId("submit");
        submitButton.scrollIntoViewIfNeeded();
        submitButton.click();
        return new SubmissionModal(page);
    }
}
