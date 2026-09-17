package com.flamingo.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/** Common helpers shared by page objects (react-select dropdowns, etc.). */
abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    /** Opens a react-select dropdown (by its container id) and picks the option with this exact text. */
    protected void selectReactSelectOption(String containerId, String optionText) {
        page.locator(containerId).click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(optionText).setExact(true)).click();
    }

    /**
     * DemoQA's ad slot containers (and the Bootstrap column that wraps them) can overlap real
     * elements and intercept clicks even when the ad content itself is blocked at the network
     * level. Neutralize the known ad slot ids so clicks pass through to the real page content
     * underneath. Call once after navigating to a DemoQA page.
     */
    protected void disableAdOverlays() {
        page.addStyleTag(new Page.AddStyleTagOptions().setContent(
                ".col-12.mt-4.col-md-3.col-xl-3, #Ad\\.Plus-970x250-2, #RightSide_Advertisement, "
                        + "#Ad\\.Plus-300x250-1, #Ad\\.Plus-300x250-2 { pointer-events: none !important; }"));
    }

    protected Locator byId(String id) {
        return page.locator("#" + id);
    }
}
