package com.flamingo.qa.api.auth;

import com.flamingo.qa.client.BookerApiClient;
import com.flamingo.qa.model.auth.AuthRequest;
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
    void createToken_withValidCredentials_returnsToken() {
        AuthRequest request = AuthRequest.builder().username("admin").password("password123").build();

        Response response = BookerApiClient.createToken(request);

        response.then().statusCode(200);
        assertThat(response.jsonPath().getString("token")).isNotBlank();
    }

    @Test
    @DisplayName("TC-AUTH-002: Authentication with invalid credentials returns no token")
    void createToken_withInvalidCredentials_returnsReasonBadCredentials() {
        AuthRequest request = AuthRequest.builder().username("admin").password("wrong-password").build();

        Response response = BookerApiClient.createToken(request);

        response.then().statusCode(200);
        assertThat(response.jsonPath().getString("token")).isNull();
        assertThat(response.jsonPath().getString("reason")).isEqualTo("Bad credentials");
    }
}
