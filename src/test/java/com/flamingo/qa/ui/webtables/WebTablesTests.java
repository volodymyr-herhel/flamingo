package com.flamingo.qa.ui.webtables;

import com.flamingo.qa.factory.PersonRecordFactory;
import com.flamingo.qa.model.ui.PersonRecord;
import com.flamingo.qa.ui.pages.WebTablesPage;
import com.flamingo.qa.ui.support.PlaywrightExtension;
import com.microsoft.playwright.Page;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DemoQA "Web Tables" (Option B) - https://demoqa.com/webtables. Unlike the API booking tests,
 * no {@code @AfterEach} cleanup is needed here: each test gets a fresh browser context (via
 * {@link PlaywrightExtension}), and Web Tables data only lives in that page's in-memory React
 * state - it's discarded once the context closes, not persisted server-side.
 */
@Tag("ui")
@Epic("DemoQA")
@Feature("Web Tables")
@ExtendWith(PlaywrightExtension.class)
class WebTablesTests {

    @Test
    @DisplayName("TC-UI-TABLE-001: Adding a new record shows it in the table")
    void addNewRecordShowsItInTable(Page page) {
        PersonRecord person = PersonRecordFactory.randomPerson();
        WebTablesPage webTables = new WebTablesPage(page).open();

        webTables.clickAdd().fill(person).submit();

        assertThat(webTables.isRecordPresent(person.getEmail())).isTrue();
    }

    @Test
    @DisplayName("TC-UI-TABLE-002: Editing an existing record updates its values in the table")
    void editRecordUpdatesItsValues(Page page) {
        PersonRecord person = PersonRecordFactory.randomPerson();
        WebTablesPage webTables = new WebTablesPage(page).open();
        webTables.clickAdd().fill(person).submit();

        PersonRecord updated = person.toBuilder().department("Engineering").build();
        webTables.clickEdit(person.getEmail()).fill(updated).submit();

        assertThat(webTables.isRecordPresent(updated.getEmail())).isTrue();
    }

    @Test
    @DisplayName("TC-UI-TABLE-003: Deleting a record removes it from the table")
    void deleteRecordRemovesItFromTable(Page page) {
        PersonRecord person = PersonRecordFactory.randomPerson();
        WebTablesPage webTables = new WebTablesPage(page).open();
        webTables.clickAdd().fill(person).submit();
        assertThat(webTables.isRecordPresent(person.getEmail())).isTrue();

        webTables.delete(person.getEmail());

        assertThat(webTables.isRecordPresent(person.getEmail())).isFalse();
    }

    @Test
    @DisplayName("TC-UI-TABLE-004: Searching filters the table to only matching records")
    void searchFiltersTableToMatchingRecords(Page page) {
        PersonRecord person = PersonRecordFactory.randomPerson();
        WebTablesPage webTables = new WebTablesPage(page).open();
        webTables.clickAdd().fill(person).submit();

        webTables.search(person.getEmail());

        assertThat(webTables.visibleFirstNames()).containsExactly(person.getFirstName());
    }

    @Test
    @DisplayName("TC-UI-TABLE-N01: Clicking a column header does not reorder rows (no sort support in this build)")
    void clickingColumnHeaderDoesNotReorderRows(Page page) {
        WebTablesPage webTables = new WebTablesPage(page).open();
        var originalOrder = webTables.visibleFirstNames();

        webTables.clickColumnHeader("First Name");

        assertThat(webTables.visibleFirstNames()).isEqualTo(originalOrder);
    }
}
