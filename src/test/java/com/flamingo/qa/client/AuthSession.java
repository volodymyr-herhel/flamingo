package com.flamingo.qa.client;

import com.flamingo.qa.factory.AuthFactory;
import io.restassured.response.Response;

/** Caches a single auth token for the whole test run so tests reuse one API session. */
public final class AuthSession {

    private static volatile String cachedToken;

    private AuthSession() {
    }

    public static synchronized String getToken() {
        if (cachedToken == null) {
            cachedToken = authenticate();
        }
        return cachedToken;
    }

    private static String authenticate() {
        Response response = BookerApiClient.createToken(AuthFactory.validCredentials());
        return response.jsonPath().getString("token");
    }
}
