package com.flamingo.qa.api.auth;

import com.flamingo.qa.client.BookerApiClient;
import com.flamingo.qa.factory.AuthFactory;
import com.flamingo.qa.model.auth.AuthResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Epic("Restful Booker API")
@Feature("Authentication")
class AuthTests {

    @Test
    @DisplayName("TC-AUTH-001: Successful authentication returns a valid token")
    void createTokenWithValidCredentials() {
        Response response = BookerApiClient.createToken(AuthFactory.validCredentials());

        response.then().statusCode(200);
        AuthResponse authResponse = response.as(AuthResponse.class);
        assertThat(authResponse.getToken()).isNotBlank();
    }

    @Test
    @DisplayName("TC-AUTH-002: Authentication with invalid credentials returns no token")
    void createTokenWithInvalidCredentials() {
        Response response = BookerApiClient.createToken(AuthFactory.invalidCredentials());

        response.then().statusCode(200);
        AuthResponse authResponse = response.as(AuthResponse.class);
        assertThat(authResponse.getToken()).isNull();
        assertThat(authResponse.getReason()).isEqualTo("Bad credentials");
    }
}
