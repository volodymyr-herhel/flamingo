package com.flamingo.qa.ui.support;

import com.flamingo.qa.config.Config;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Optional;

/**
 * Manages the Playwright browser lifecycle for UI tests: one {@link Browser} shared for the
 * whole run (like {@code AuthSession} does for the API token), a fresh {@link BrowserContext}/
 * {@link Page} per test method for isolation, and a screenshot attached to Allure whenever a
 * test fails. Inject a {@link Page} by adding it as a test method parameter.
 */
public class PlaywrightExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver, TestWatcher {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PlaywrightExtension.class);
    private static volatile Playwright playwright;
    private static volatile Browser browser;

    // DemoQA loads third-party ad scripts/iframes that add noticeable network noise and page-load
    // delay; block the known ad-serving domains so pages settle faster. This does NOT fix ad
    // slots intercepting clicks - that's handled separately by BasePage.disableAdOverlays(),
    // since the reserved ad-slot layout div stays in the DOM even when its content is blocked.
    private static final String[] BLOCKED_AD_HOSTS = {
            "doubleclick.net", "googlesyndication.com", "google-analytics.com",
            "googletagservices.com", "adservice.google.com", "mediago.io", "media.net"
    };

    @Override
    public void beforeEach(ExtensionContext context) {
        BrowserContext browserContext = getBrowser().newContext();
        browserContext.route("**/*", route -> {
            String url = route.request().url();
            boolean isAd = Arrays.stream(BLOCKED_AD_HOSTS).anyMatch(url::contains);
            if (isAd) {
                route.abort();
            } else {
                route.resume();
            }
        });
        Page page = browserContext.newPage();
        ExtensionContext.Store store = context.getStore(NAMESPACE);
        store.put(BrowserContext.class, browserContext);
        store.put(Page.class, page);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        BrowserContext browserContext = context.getStore(NAMESPACE).get(BrowserContext.class, BrowserContext.class);
        if (browserContext != null) {
            browserContext.close();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Page.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(Page.class, Page.class);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Optional.ofNullable(context.getStore(NAMESPACE).get(Page.class, Page.class))
                .ifPresent(page -> {
                    byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                    Allure.addAttachment(context.getDisplayName() + " - failure screenshot", "image/png",
                            new ByteArrayInputStream(screenshot), "png");
                });
    }

    private static Browser getBrowser() {
        if (browser == null) {
            synchronized (PlaywrightExtension.class) {
                if (browser == null) {
                    playwright = Playwright.create();
                    browser = playwright.chromium().launch(
                            new BrowserType.LaunchOptions().setHeadless(Config.PLAYWRIGHT_HEADLESS));
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        browser.close();
                        playwright.close();
                    }));
                }
            }
        }
        return browser;
    }
}
