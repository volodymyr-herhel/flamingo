package com.flamingo.qa.factory;

import com.flamingo.qa.model.auth.AuthRequest;

/** Single source of truth for the Restful Booker demo credentials, used by both tests and {@code AuthSession}. */
public final class AuthFactory {

    public static final String VALID_USERNAME = "admin";
    public static final String VALID_PASSWORD = "password123";

    private AuthFactory() {
    }

    public static AuthRequest validCredentials() {
        return AuthRequest.builder().username(VALID_USERNAME).password(VALID_PASSWORD).build();
    }

    public static AuthRequest invalidCredentials() {
        return AuthRequest.builder().username(VALID_USERNAME).password("wrong-password").build();
    }
}
