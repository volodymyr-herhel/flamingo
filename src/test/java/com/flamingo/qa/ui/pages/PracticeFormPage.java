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

    private static final String STATE_DROPDOWN = "#state";
    private static final String CITY_DROPDOWN = "#city";

    private final Locator firstNameInput = byId("firstName");
    private final Locator lastNameInput = byId("lastName");
    private final Locator emailInput = byId("userEmail");
    private final Locator mobileNumberInput = byId("userNumber");
    private final Locator dateOfBirthInput = byId("dateOfBirthInput");
    private final Locator monthSelect = page.locator(".react-datepicker__month-select");
    private final Locator yearSelect = page.locator(".react-datepicker__year-select");
    private final Locator subjectsInput = byId("subjectsInput");
    private final Locator currentAddressInput = byId("currentAddress");
    private final Locator uploadPictureInput = byId("uploadPicture");
    private final Locator submitButton = byId("submit");

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
        firstNameInput.fill(student.getFirstName());
        lastNameInput.fill(student.getLastName());
        emailInput.fill(student.getEmail());
        genderRadio(student.getGender()).check();
        mobileNumberInput.fill(student.getMobileNumber());
        setDateOfBirth(student.getDateOfBirth());
        student.getSubjects().forEach(this::addSubject);
        student.getHobbies().forEach(hobby -> hobbyCheckbox(hobby).check());
        currentAddressInput.fill(student.getCurrentAddress());
        selectReactSelectOption(STATE_DROPDOWN, student.getState());
        selectReactSelectOption(CITY_DROPDOWN, student.getCity());
        return this;
    }

    @Step("Upload a picture")
    public PracticeFormPage uploadPicture(Path filePath) {
        uploadPictureInput.setInputFiles(filePath);
        return this;
    }

    @Step("Set date of birth: {date}")
    public PracticeFormPage setDateOfBirth(LocalDate date) {
        dateOfBirthInput.click();
        monthSelect.selectOption(String.valueOf(date.getMonthValue() - 1));
        yearSelect.selectOption(String.valueOf(date.getYear()));
        dayOption(date).click();
        return this;
    }

    @Step("Add subject: {subject}")
    public PracticeFormPage addSubject(String subject) {
        subjectsInput.fill(subject);
        subjectsInput.press("Enter");
        return this;
    }

    @Step("Submit the form")
    public SubmissionModal submit() {
        submitButton.scrollIntoViewIfNeeded();
        submitButton.click();
        return new SubmissionModal(page);
    }

    private Locator genderRadio(Gender gender) {
        return byId("gender-radio-" + gender.getRadioIndex());
    }

    private Locator hobbyCheckbox(Hobby hobby) {
        return byId("hobbies-checkbox-" + hobby.getCheckboxIndex());
    }

    private Locator dayOption(LocalDate date) {
        return page.locator(".react-datepicker__day--" + String.format("%03d", date.getDayOfMonth())
                + ":not(.react-datepicker__day--outside-month)");
    }
}
