package com.flamingo.qa.client;

import io.restassured.response.Response;

import java.util.Set;
import java.util.function.Supplier;

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
        Response response = call.get();
        int attempt = 1;
        long backoff = INITIAL_BACKOFF_MILLIS;
        while (RETRYABLE_STATUS_CODES.contains(response.statusCode()) && attempt < MAX_ATTEMPTS) {
            sleep(backoff);
            backoff *= 2;
            response = call.get();
            attempt++;
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
