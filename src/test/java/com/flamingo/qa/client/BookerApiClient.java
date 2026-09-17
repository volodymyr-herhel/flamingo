package com.flamingo.qa.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flamingo.qa.config.Config;
import com.flamingo.qa.model.auth.AuthRequest;
import com.flamingo.qa.model.booking.Booking;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/** Thin wrapper around the Restful Booker REST endpoints. */
public final class BookerApiClient {

    // The public API's edge blocks requests without a User-Agent (returns HTTP 418), and some
    // endpoints reply with a text/plain Content-Type even though the body is JSON. It also
    // deterministically 418s RestAssured's default multi-value Accept header from .accept(JSON)
    // ("application/json, application/javascript, text/javascript, text/json") as a bot
    // signature - baseSpec() below sends a plain "application/json" Accept value instead.
    private static final String USER_AGENT = "Mozilla/5.0 (FlamingoQA-Automation)";

    static {
        AllureReportingSupport.ensureRegistered();
        RestAssured.registerParser("text/plain", Parser.JSON);
        // LocalDate fields (BookingDates) need java.time support to (de)serialize as yyyy-MM-dd.
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory((type, s) ->
                        new ObjectMapper()
                                .registerModule(new JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)));
    }

    private BookerApiClient() {
    }

    private static RequestSpecification baseSpec() {
        return RestAssured.given()
                .baseUri(Config.BOOKER_BASE_URL)
                .header("User-Agent", USER_AGENT)
                .contentType(ContentType.JSON)
                .header("Accept", "application/json");
    }

    @Step("POST /auth - create token")
    public static Response createToken(AuthRequest request) {
        return RequestRetrySupport.withRetry(() -> baseSpec().body(request).when().post("/auth"));
    }

    @Step("GET /booking - list booking ids")
    public static Response getBookingIds(Map<String, String> queryParams) {
        return RequestRetrySupport.withRetry(() -> baseSpec()
                .queryParams(queryParams == null ? Map.of() : queryParams)
                .when().get("/booking"));
    }

    @Step("GET /booking/{bookingId}")
    public static Response getBooking(int bookingId) {
        return RequestRetrySupport.withRetry(() -> baseSpec().when().get("/booking/{id}", bookingId));
    }

    @Step("POST /booking - create booking")
    public static Response createBooking(Booking booking) {
        return RequestRetrySupport.withRetry(() -> baseSpec().body(booking).when().post("/booking"));
    }

    @Step("POST /booking - create booking with raw body")
    public static Response createBookingRaw(String rawJsonBody) {
        return RequestRetrySupport.withRetry(() -> baseSpec().body(rawJsonBody).when().post("/booking"));
    }

    @Step("PUT /booking/{bookingId} - update booking with a valid token")
    public static Response updateBooking(int bookingId, Booking booking, String token) {
        return RequestRetrySupport.withRetry(() -> baseSpec()
                .header("Cookie", "token=" + token)
                .body(booking)
                .when().put("/booking/{id}", bookingId));
    }

    @Step("PUT /booking/{bookingId} - update booking with Basic auth")
    public static Response updateBookingWithBasicAuth(int bookingId, Booking booking, String username, String password) {
        String basicAuth = Base64.getEncoder().encodeToString(
                (username + ":" + password).getBytes(StandardCharsets.UTF_8));
        return RequestRetrySupport.withRetry(() -> baseSpec()
                .header("Authorization", "Basic " + basicAuth)
                .body(booking)
                .when().put("/booking/{id}", bookingId));
    }

    @Step("PATCH /booking/{bookingId} - partial update booking")
    public static Response partialUpdateBooking(int bookingId, Map<String, Object> fields, String token) {
        return RequestRetrySupport.withRetry(() -> baseSpec()
                .header("Cookie", "token=" + token)
                .body(fields)
                .when().patch("/booking/{id}", bookingId));
    }

    @Step("PUT /booking/{bookingId} - update booking without auth")
    public static Response updateBookingWithoutAuth(int bookingId, Booking booking) {
        return RequestRetrySupport.withRetry(() -> baseSpec().body(booking).when().put("/booking/{id}", bookingId));
    }

    @Step("DELETE /booking/{bookingId}")
    public static Response deleteBooking(int bookingId, String token) {
        return RequestRetrySupport.withRetry(() -> baseSpec()
                .header("Cookie", "token=" + token)
                .when().delete("/booking/{id}", bookingId));
    }

    @Step("DELETE /booking/{bookingId} without auth")
    public static Response deleteBookingWithoutAuth(int bookingId) {
        return RequestRetrySupport.withRetry(() -> baseSpec().when().delete("/booking/{id}", bookingId));
    }

    @Step("GET /ping - health check")
    public static Response ping() {
        return RequestRetrySupport.withRetry(() -> baseSpec().when().get("/ping"));
    }
}
