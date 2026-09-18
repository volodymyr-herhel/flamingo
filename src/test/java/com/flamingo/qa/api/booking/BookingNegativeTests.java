package com.flamingo.qa.api.booking;

import com.flamingo.qa.client.BookerApiClient;
import com.flamingo.qa.factory.BookingFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Each test that needs a real booking creates its own and deletes it in
 * {@link BookingTestSupport#cleanupBooking()} afterwards, so tests don't depend on each other or
 * on execution order.
 */
@Tag("api")
@Epic("Restful Booker API")
@Feature("Booking Negative Scenarios")
class BookingNegativeTests extends BookingTestSupport {

    private static final int NON_EXISTENT_BOOKING_ID = 999_999_999;

    @Test
    @DisplayName("TC-BOOK-N01: Get booking with a non-existent id returns 404")
    void getBookingWithNonExistentId() {
        Response response = BookerApiClient.getBooking(NON_EXISTENT_BOOKING_ID);

        response.then().statusCode(404);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("unauthenticatedRequests")
    void unauthenticatedRequestReturns403(String scenario, Function<Integer, Response> request) {
        createBooking(BookingFactory.randomBooking());

        Response response = request.apply(bookingId);

        response.then().statusCode(403);
    }

    private static Stream<Arguments> unauthenticatedRequests() {
        return Stream.of(
                Arguments.of("TC-BOOK-N02: Update booking without an auth token returns 403",
                        (Function<Integer, Response>) id ->
                                BookerApiClient.updateBookingWithoutAuth(id, BookingFactory.randomBooking())),
                Arguments.of("TC-BOOK-N03: Delete booking without an auth token returns 403",
                        (Function<Integer, Response>) BookerApiClient::deleteBookingWithoutAuth)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("malformedJsonBodies")
    @DisplayName("TC-BOOK-N04: Create booking with a malformed JSON body returns a client error")
    void createBookingWithMalformedJsonBody(String scenario, String malformedJson) {
        Response response = BookerApiClient.createBookingRaw(malformedJson);

        assertThat(response.statusCode()).isBetween(400, 499);
    }

    private static Stream<Arguments> malformedJsonBodies() {
        return Stream.of(
                Arguments.of("truncated object", "{ \"firstname\": \"John\", \"lastname\": "),
                Arguments.of("trailing comma", "{ \"firstname\": \"John\", \"lastname\": \"Doe\", }"),
                Arguments.of("unquoted key", "{ firstname: \"John\" }")
        );
    }
}
