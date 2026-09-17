package com.flamingo.qa.client;

import com.flamingo.qa.model.auth.AuthRequest;
import io.restassured.response.Response;

/** Caches a single auth token for the whole test run so tests reuse one API session. */
public final class AuthSession {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123";

    private static volatile String cachedToken;

    private AuthSession() {
    }

    public static synchronized String getToken() {
        if (cachedToken == null) {
            cachedToken = authenticate();
        }
        return cachedToken;
    }

    /** Forces the next {@link #getToken()} call to authenticate again. */
    public static synchronized void invalidate() {
        cachedToken = null;
    }

    private static String authenticate() {
        AuthRequest request = AuthRequest.builder().username(USERNAME).password(PASSWORD).build();
        Response response = BookerApiClient.createToken(request);
        return response.jsonPath().getString("token");
    }
}
