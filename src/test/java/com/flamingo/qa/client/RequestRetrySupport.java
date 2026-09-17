package com.flamingo.qa.client;

import io.qameta.allure.Allure;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Restful Booker is a shared public demo API that occasionally answers rapid bursts of
 * requests with a transient 418/429 instead of the real response. Retrying a couple of
 * times with a short backoff keeps the suite stable without hammering the service.
 */
final class RequestRetrySupport {

    private static final Set<Integer> RETRYABLE_STATUS_CODES = Set.of(418, 429);
    private static final int MAX_ATTEMPTS = 5;
    private static final long INITIAL_BACKOFF_MILLIS = 1000;

    private RequestRetrySupport() {
    }

    static Response withRetry(Supplier<Response> call) {
        List<Integer> statusesSeen = new ArrayList<>();
        Response response = call.get();
        statusesSeen.add(response.statusCode());
        int attempt = 1;
        long backoff = INITIAL_BACKOFF_MILLIS;
        while (RETRYABLE_STATUS_CODES.contains(response.statusCode()) && attempt < MAX_ATTEMPTS) {
            sleep(backoff);
            backoff *= 2;
            response = call.get();
            statusesSeen.add(response.statusCode());
            attempt++;
        }
        if (RETRYABLE_STATUS_CODES.contains(response.statusCode())) {
            // @BeforeAll fixture failures don't get their own "Execution" timeline in Allure, so
            // attach the retry history directly and put it in the exception message too - both
            // need to be self-explanatory without digging into per-attempt request/response steps.
            String statusHistory = statusesSeen.stream().map(String::valueOf).collect(Collectors.joining(", "));
            String summary = "Restful Booker still rate-limited after " + MAX_ATTEMPTS
                    + " attempts (statuses: [" + statusHistory + "]); last response body was: \""
                    + response.getBody().asString() + "\" -- this is shared public-API throttling, not a code"
                    + " defect; wait a bit and re-run.";
            Allure.addAttachment("Rate-limit retry summary", "text/plain", summary);
            throw new IllegalStateException(summary);
        }
        return response;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
