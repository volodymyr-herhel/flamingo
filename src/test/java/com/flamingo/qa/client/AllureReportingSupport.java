package com.flamingo.qa.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

import java.util.concurrent.atomic.AtomicBoolean;

/** Ensures the Allure REST Assured filter is registered exactly once across all API clients. */
public final class AllureReportingSupport {

    private static final AtomicBoolean REGISTERED = new AtomicBoolean(false);

    private AllureReportingSupport() {
    }

    public static void ensureRegistered() {
        if (REGISTERED.compareAndSet(false, true)) {
            RestAssured.filters(new AllureRestAssured());
        }
    }
}
