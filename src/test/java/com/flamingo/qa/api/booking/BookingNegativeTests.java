package com.flamingo.qa.api.booking;

import com.flamingo.qa.client.BookerApiClient;
import com.flamingo.qa.factory.BookingFactory;
import com.flamingo.qa.model.booking.CreateBookingResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Both auth-negative scenarios below are rejected before any mutation happens, so they safely
 * share the same booking (created once) instead of each provisioning their own.
 */
@Tag("api")
@Epic("Restful Booker API")
@Feature("Booking Negative Scenarios")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingNegativeTests {

    private static final int NON_EXISTENT_BOOKING_ID = 999_999_999;

    private int existingBookingId;

    @BeforeAll
    void createBookingForAuthTests() {
        existingBookingId = BookerApiClient.createBooking(BookingFactory.randomBooking())
                .as(CreateBookingResponse.class).getBookingid();
    }

    @Test
    @DisplayName("TC-BOOK-N01: Get booking with a non-existent id returns 404")
    void getBookingWithNonExistentId() {
        Response response = BookerApiClient.getBooking(NON_EXISTENT_BOOKING_ID);

        response.then().statusCode(404);
    }

    @Test
    @DisplayName("TC-BOOK-N02: Update booking without an auth token returns 403")
    void updateBookingWithoutAuthToken() {
        Response response = BookerApiClient.updateBookingWithoutAuth(existingBookingId, BookingFactory.randomBooking());

        response.then().statusCode(403);
    }

    @Test
    @DisplayName("TC-BOOK-N03: Delete booking without an auth token returns 403")
    void deleteBookingWithoutAuthToken() {
        Response response = BookerApiClient.deleteBookingWithoutAuth(existingBookingId);

        response.then().statusCode(403);
    }

    @Test
    @DisplayName("TC-BOOK-N04: Create booking with a malformed JSON body returns a client error")
    void createBookingWithMalformedJsonBody() {
        String malformedJson = "{ \"firstname\": \"John\", \"lastname\": ";

        Response response = BookerApiClient.createBookingRaw(malformedJson);

        assertThat(response.statusCode()).isBetween(400, 499);
    }
}
