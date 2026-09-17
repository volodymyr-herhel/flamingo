package com.flamingo.qa.ui.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/** Wraps the "Thanks for submitting the form" success modal on the practice form page. */
public class SubmissionModal extends BasePage {

    public SubmissionModal(Page page) {
        super(page);
    }

    public boolean isVisible() {
        return byId("example-modal-sizes-title-lg").isVisible();
    }

    /** Reads the value cell for a given label row (e.g. "Student Name", "Gender") from the summary table. */
    public String getValue(String label) {
        return page.locator(".modal-content tbody tr", new Page.LocatorOptions().setHasText(label))
                .locator("td").nth(1).textContent();
    }

    public void close() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Close")).click();
    }
}
