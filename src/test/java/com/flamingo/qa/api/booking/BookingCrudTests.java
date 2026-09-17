package com.flamingo.qa.api.booking;

import com.flamingo.qa.client.AuthSession;
import com.flamingo.qa.client.BookerApiClient;
import com.flamingo.qa.factory.AuthFactory;
import com.flamingo.qa.factory.BookingFactory;
import com.flamingo.qa.model.booking.Booking;
import com.flamingo.qa.model.booking.BookingId;
import com.flamingo.qa.model.booking.CreateBookingResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers the full booking CRUD lifecycle. Each test creates the booking it needs and deletes it
 * in {@link BookingTestSupport#cleanupBooking()} afterwards, so tests don't depend on each
 * other or on execution order.
 */
@Tag("api")
@Epic("Restful Booker API")
@Feature("Booking CRUD")
class BookingCrudTests extends BookingTestSupport {

    @Test
    @DisplayName("TC-BOOK-001: Create booking with valid data returns the created booking")
    void createBookingWithValidData() {
        Booking booking = BookingFactory.randomBooking();

        Response response = BookerApiClient.createBooking(booking);

        response.then().statusCode(200);
        CreateBookingResponse created = response.as(CreateBookingResponse.class);
        assertThat(created.getBookingid()).isPositive();
        assertThat(created.getBooking()).isEqualTo(booking);

        bookingId = created.getBookingid();
    }

    @Test
    @DisplayName("TC-BOOK-002: Get booking by id returns the matching booking data")
    void getBookingWithValidId() {
        Booking booking = BookingFactory.randomBooking();
        createBooking(booking);

        Response response = BookerApiClient.getBooking(bookingId);

        response.then().statusCode(200);
        assertThat(response.as(Booking.class)).isEqualTo(booking);
    }

    @Test
    @DisplayName("TC-BOOK-006: Get booking ids without filters returns a non-empty list")
    void getBookingIdsWithoutFilters() {
        Response response = BookerApiClient.getBookingIds(null);

        response.then().statusCode(200);
        List<BookingId> ids = response.jsonPath().getList("", BookingId.class);
        assertThat(ids).isNotEmpty();
    }

    @Test
    @DisplayName("TC-BOOK-004: Partial update with a valid token only changes provided fields")
    void partialUpdateBookingWithValidToken() {
        Booking booking = BookingFactory.randomBooking();
        createBooking(booking);
        String newFirstname = "Flamingo" + System.currentTimeMillis();

        Response response = BookerApiClient.partialUpdateBooking(
                bookingId, Map.of("firstname", newFirstname), AuthSession.getToken());

        response.then().statusCode(200);
        Booking updated = response.as(Booking.class);
        assertThat(updated.getFirstname()).isEqualTo(newFirstname);
        assertThat(updated.getLastname()).isEqualTo(booking.getLastname());
        assertThat(updated.getTotalprice()).isEqualTo(booking.getTotalprice());
    }

    @Test
    @DisplayName("TC-BOOK-007: Get booking ids filtered by first/last name includes the matching booking")
    void getBookingIdsFilteredByFirstNameAndLastName() {
        Booking booking = BookingFactory.randomBooking();
        createBooking(booking);

        Response response = BookerApiClient.getBookingIds(
                Map.of("firstname", booking.getFirstname(), "lastname", booking.getLastname()));

        response.then().statusCode(200);
        List<BookingId> ids = response.jsonPath().getList("", BookingId.class);
        assertThat(ids).extracting(BookingId::getBookingid).contains(bookingId);
    }

    @Test
    @DisplayName("TC-BOOK-008: Get booking ids filtered by checkin/checkout date returns a valid list")
    void getBookingIdsFilteredByCheckinAndCheckout() {
        Booking booking = BookingFactory.randomBooking();
        createBooking(booking);

        // Restful Booker's checkin/checkout filter is known to be unreliable on the shared demo
        // instance (it can return bookings that don't match the requested dates at all), so this
        // only asserts the endpoint accepts the date params and returns a well-formed list -
        // it doesn't assert our booking is present.
        Response response = BookerApiClient.getBookingIds(Map.of(
                "checkin", booking.getBookingdates().getCheckin().toString(),
                "checkout", booking.getBookingdates().getCheckout().toString()));

        response.then().statusCode(200);
        assertThat(response.jsonPath().getList("", BookingId.class)).isNotNull();
    }

    @Test
    @DisplayName("TC-BOOK-003: Update booking with a valid token updates all fields")
    void updateBookingWithValidTokenAndData() {
        createBooking(BookingFactory.randomBooking());
        Booking updatedBooking = BookingFactory.randomBooking();

        Response response = BookerApiClient.updateBooking(bookingId, updatedBooking, AuthSession.getToken());

        response.then().statusCode(200);
        assertThat(response.as(Booking.class)).isEqualTo(updatedBooking);
    }

    @Test
    @DisplayName("TC-BOOK-009: Update booking (PUT) with Basic auth updates all fields")
    void updateBookingWithBasicAuth() {
        createBooking(BookingFactory.randomBooking());
        Booking updatedBooking = BookingFactory.randomBooking();

        Response response = BookerApiClient.updateBookingWithBasicAuth(
                bookingId, updatedBooking, AuthFactory.VALID_USERNAME, AuthFactory.VALID_PASSWORD);

        response.then().statusCode(200);
        assertThat(response.as(Booking.class)).isEqualTo(updatedBooking);
    }

    @Test
    @DisplayName("TC-BOOK-005: Delete booking with a valid token removes the booking")
    void deleteBookingWithValidToken() {
        createBooking(BookingFactory.randomBooking());

        Response deleteResponse = BookerApiClient.deleteBooking(bookingId, AuthSession.getToken());
        deleteResponse.then().statusCode(201);

        BookerApiClient.getBooking(bookingId).then().statusCode(404);

        bookingId = null; // already deleted - skip the redundant cleanup delete
    }
}
