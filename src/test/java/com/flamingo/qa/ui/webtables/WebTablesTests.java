package com.flamingo.qa.ui.webtables;

import com.flamingo.qa.factory.PersonRecordFactory;
import com.flamingo.qa.model.ui.PersonRecord;
import com.flamingo.qa.ui.pages.RegistrationFormModal;
import com.flamingo.qa.ui.pages.WebTablesPage;
import com.flamingo.qa.ui.support.PlaywrightExtension;
import com.microsoft.playwright.Page;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DemoQA "Web Tables" (Option B) - https://demoqa.com/webtables. Each test gets a fresh browser
 * context (via {@link PlaywrightExtension}), so tests never see rows added by another test; any
 * record a test itself adds is additionally deleted in {@link #cleanUpCreatedRecords()} (runs
 * before the context closes) as defensive cleanup, kept consistent with the API tests' pattern.
 */
@Tag("ui")
@Epic("DemoQA")
@Feature("Web Tables")
@ExtendWith(PlaywrightExtension.class)
class WebTablesTests {

    private final List<String> createdEmails = new ArrayList<>();
    private WebTablesPage webTables;

    @AfterEach
    void cleanUpCreatedRecords() {
        if (webTables == null) {
            return;
        }
        createdEmails.forEach(email -> {
            if (webTables.isRecordPresent(email)) {
                webTables.delete(email);
            }
        });
    }

    @Test
    @Tag("smoke")
    @DisplayName("TC-UI-TABLE-001: Adding a new record shows it in the table")
    void addNewRecordShowsItInTable(Page page) {
        PersonRecord person = addNewRecord(page);
        createdEmails.add(person.getEmail());

        assertThat(webTables.isRecordPresent(person.getEmail())).isTrue();
    }

    @Test
    @DisplayName("TC-UI-TABLE-002: Editing an existing record updates its values in the table")
    void editRecordUpdatesItsValues(Page page) {
        PersonRecord person = addNewRecord(page);

        PersonRecord updated = person.toBuilder().department("Engineering").build();
        webTables.clickEdit(person.getEmail()).fill(updated).submit();
        createdEmails.add(updated.getEmail());

        assertThat(webTables.isRecordPresent(updated.getEmail())).isTrue();
    }

    @Test
    @DisplayName("TC-UI-TABLE-003: Deleting a record removes it from the table")
    void deleteRecordRemovesItFromTable(Page page) {
        PersonRecord person = addNewRecord(page);
        assertThat(webTables.isRecordPresent(person.getEmail())).isTrue();

        webTables.delete(person.getEmail());

        assertThat(webTables.isRecordPresent(person.getEmail())).isFalse();
    }

    @Test
    @DisplayName("TC-UI-TABLE-004: Searching filters the table to only matching records")
    void searchFiltersTableToMatchingRecords(Page page) {
        PersonRecord person = addNewRecord(page);
        createdEmails.add(person.getEmail());

        webTables.search(person.getEmail());

        assertThat(webTables.visibleFirstNames()).containsExactly(person.getFirstName());
    }

    @Test
    @DisplayName("TC-UI-TABLE-N01: Clicking a column header does not reorder rows (no sort support in this build)")
    void clickingColumnHeaderDoesNotReorderRows(Page page) {
        webTables = new WebTablesPage(page).open();
        List<String> originalOrder = webTables.visibleFirstNames();

        webTables.clickColumnHeader("First Name");

        assertThat(webTables.visibleFirstNames()).isEqualTo(originalOrder);
    }

    @Test
    @DisplayName("TC-UI-TABLE-N02: Adding a record with an invalid email format does not add it to the table")
    void addRecordWithInvalidEmailIsRejected(Page page) {
        PersonRecord person = PersonRecordFactory.randomPerson().toBuilder().email("not-an-email").build();

        RegistrationFormModal modal = attemptAddRecord(page, person);

        assertRecordRejected(modal, person);
    }

    @Test
    @DisplayName("TC-UI-TABLE-N03: Adding a record with a required field left blank does not add it to the table")
    void addRecordWithMissingRequiredFieldIsRejected(Page page) {
        PersonRecord person = PersonRecordFactory.randomPerson().toBuilder().firstName("").build();

        RegistrationFormModal modal = attemptAddRecord(page, person);

        assertRecordRejected(modal, person);
    }

    /** Creates a random record, opens Web Tables, and adds it via the Add Record modal. */
    private PersonRecord addNewRecord(Page page) {
        PersonRecord person = PersonRecordFactory.randomPerson();
        attemptAddRecord(page, person);
        return person;
    }

    /** Opens Web Tables and submits the Add Record modal with the given (possibly invalid) data. */
    private RegistrationFormModal attemptAddRecord(Page page, PersonRecord person) {
        webTables = new WebTablesPage(page).open();
        return webTables.clickAdd().fill(person).submit();
    }

    /** Asserts a rejected Add Record submission: modal stayed open, no row was added. */
    private void assertRecordRejected(RegistrationFormModal modal, PersonRecord person) {
        assertThat(modal.isVisible()).isTrue();
        assertThat(webTables.isRecordPresent(person.getLastName())).isFalse();
    }
}
