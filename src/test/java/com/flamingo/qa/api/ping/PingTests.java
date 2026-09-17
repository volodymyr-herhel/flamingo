package com.flamingo.qa.api.ping;

import com.flamingo.qa.client.BookerApiClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("api")
@Epic("Restful Booker API")
@Feature("Health Check")
class PingTests {

    @Test
    @DisplayName("TC-PING-001: Health check endpoint is reachable")
    void ping_healthCheck_returns201() {
        Response response = BookerApiClient.ping();

        response.then().statusCode(201);
    }
}
