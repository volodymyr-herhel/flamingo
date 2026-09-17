package com.flamingo.qa.ui.form;

import com.flamingo.qa.factory.StudentDetailsFactory;
import com.flamingo.qa.model.ui.StudentDetails;
import com.flamingo.qa.ui.pages.PracticeFormPage;
import com.flamingo.qa.ui.pages.SubmissionModal;
import com.flamingo.qa.ui.support.PlaywrightExtension;
import com.microsoft.playwright.Page;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/** DemoQA "Student Registration Form" (Option A) - https://demoqa.com/automation-practice-form. */
@Tag("ui")
@Epic("DemoQA")
@Feature("Practice Form")
@ExtendWith(PlaywrightExtension.class)
class PracticeFormTests {

    private static final Path SAMPLE_UPLOAD_FILE =
            Path.of("src/test/resources/ui/sample-upload.txt").toAbsolutePath();
    private static final DateTimeFormatter MODAL_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d MMMM,yyyy", Locale.ENGLISH);

    @Test
    @DisplayName("TC-UI-FORM-001: Submitting a fully filled form shows the correct data in the success modal")
    void submitFullyFilledFormShowsMatchingSummary(Page page) {
        StudentDetails student = StudentDetailsFactory.randomStudent();

        SubmissionModal modal = new PracticeFormPage(page)
                .open()
                .fillStudentDetails(student)
                .uploadPicture(SAMPLE_UPLOAD_FILE)
                .submit();

        assertThat(modal.isVisible()).isTrue();
        assertThat(modal.getValue("Student Name")).isEqualTo(student.fullName());
        assertThat(modal.getValue("Student Email")).isEqualTo(student.getEmail());
        assertThat(modal.getValue("Gender")).isEqualToIgnoringCase(student.getGender().name());
        assertThat(modal.getValue("Mobile")).isEqualTo(student.getMobileNumber());
        assertThat(modal.getValue("Date of Birth")).isEqualTo(student.getDateOfBirth().format(MODAL_DATE_FORMAT));
        assertThat(modal.getValue("Subjects")).isEqualTo(String.join(", ", student.getSubjects()));
        assertThat(modal.getValue("Picture")).isEqualTo(SAMPLE_UPLOAD_FILE.getFileName().toString());
        assertThat(modal.getValue("Address")).isEqualTo(student.getCurrentAddress());
        assertThat(modal.getValue("State and City")).isEqualTo(student.getState() + " " + student.getCity());
    }

    @Test
    @DisplayName("TC-UI-FORM-N01: Submitting the form without any required data shows no success modal")
    void submitEmptyFormShowsNoSuccessModal(Page page) {
        SubmissionModal modal = new PracticeFormPage(page).open().submit();

        assertThat(modal.isVisible()).isFalse();
    }
}
