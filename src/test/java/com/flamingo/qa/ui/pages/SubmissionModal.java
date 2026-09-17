package com.flamingo.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/** Wraps the "Thanks for submitting the form" success modal on the practice form page. */
public class SubmissionModal extends BasePage {

    private final Locator modalTitle = byId("example-modal-sizes-title-lg");

    public SubmissionModal(Page page) {
        super(page);
    }

    public boolean isVisible() {
        return modalTitle.isVisible();
    }

    /** Reads the value cell for a given label row (e.g. "Student Name", "Gender") from the summary table. */
    public String getValue(String label) {
        return summaryRow(label).locator("td").nth(1).textContent();
    }

    private Locator summaryRow(String label) {
        return withText(".modal-content tbody tr", label);
    }
}
