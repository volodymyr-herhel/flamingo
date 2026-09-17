package com.flamingo.qa.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/** Common helpers shared by page objects (react-select dropdowns, etc.). */
abstract class BasePage {

    /** DemoQA ad slot containers (and the Bootstrap column wrapping them) that intercept clicks. */
    private static final String AD_OVERLAY_SELECTOR =
            ".col-12.mt-4.col-md-3.col-xl-3, #Ad\\.Plus-970x250-2, #RightSide_Advertisement, "
                    + "#Ad\\.Plus-300x250-1, #Ad\\.Plus-300x250-2";

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    /** Opens a react-select dropdown (by its container id) and picks the option with this exact text. */
    protected void selectReactSelectOption(String containerId, String optionText) {
        dropdownContainer(containerId).click();
        dropdownOption(optionText).click();
    }

    /**
     * DemoQA's ad slot containers (and the Bootstrap column that wraps them) can overlap real
     * elements and intercept clicks even when the ad content itself is blocked at the network
     * level. Neutralize the known ad slot ids so clicks pass through to the real page content
     * underneath. Call once after navigating to a DemoQA page.
     */
    protected void disableAdOverlays() {
        page.addStyleTag(new Page.AddStyleTagOptions().setContent(
                AD_OVERLAY_SELECTOR + " { pointer-events: none !important; }"));
    }

    protected Locator byId(String id) {
        return page.locator("#" + id);
    }

    /** Locates elements matching {@code selector} whose text content contains {@code text}. */
    protected Locator withText(String selector, String text) {
        return page.locator(selector, new Page.LocatorOptions().setHasText(text));
    }

    private Locator dropdownContainer(String containerId) {
        return page.locator(containerId);
    }

    private Locator dropdownOption(String optionText) {
        return page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(optionText).setExact(true));
    }
}
