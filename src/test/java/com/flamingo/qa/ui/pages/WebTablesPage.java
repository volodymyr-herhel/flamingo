package com.flamingo.qa.ui.pages;

import com.flamingo.qa.config.Config;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.util.List;

/** Page object for https://demoqa.com/webtables. */
public class WebTablesPage extends BasePage {

    public WebTablesPage(Page page) {
        super(page);
    }

    @Step("Open the web tables page")
    public WebTablesPage open() {
        page.navigate(Config.DEMOQA_BASE_URL + "/webtables");
        disableAdOverlays();
        return this;
    }

    @Step("Click Add")
    public RegistrationFormModal clickAdd() {
        byId("addNewRecordButton").click();
        return new RegistrationFormModal(page);
    }

    @Step("Edit record with email: {email}")
    public RegistrationFormModal clickEdit(String email) {
        rowContaining(email).locator("[id^='edit-record-']").click();
        return new RegistrationFormModal(page);
    }

    @Step("Delete record with email: {email}")
    public void delete(String email) {
        rowContaining(email).locator("[id^='delete-record-']").click();
    }

    @Step("Search for: {query}")
    public WebTablesPage search(String query) {
        byId("searchBox").fill(query);
        return this;
    }

    public boolean isRecordPresent(String email) {
        return rowContaining(email).count() > 0;
    }

    public List<String> visibleFirstNames() {
        return page.locator("table tbody tr td:first-child").allTextContents();
    }

    /** Clicks the given column header; DemoQA's current Web Tables page has no working sort. */
    @Step("Click column header: {columnName}")
    public WebTablesPage clickColumnHeader(String columnName) {
        page.locator("table thead th", new Page.LocatorOptions().setHasText(columnName)).click();
        return this;
    }

    private Locator rowContaining(String text) {
        return page.locator("table tbody tr", new Page.LocatorOptions().setHasText(text));
    }
}
