package com.flamingo.qa.api.booking;

import com.flamingo.qa.client.AuthSession;
import com.flamingo.qa.client.BookerApiClient;
import com.flamingo.qa.factory.BookingFactory;
import com.flamingo.qa.model.booking.Booking;
import com.flamingo.qa.model.booking.CreateBookingResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers the full booking CRUD lifecycle. Most tests share a single booking created once in
 * {@link #createSharedBooking()} and run in a fixed order, so the suite sends as few requests
 * as possible to the shared public API while still exercising every endpoint independently.
 */
@Tag("api")
@Epic("Restful Booker API")
@Feature("Booking CRUD")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingCrudTests {

    private int sharedBookingId;
    private Booking sharedBooking;
    private String uniqueFirstname;

    @BeforeAll
    void createSharedBooking() {
        sharedBooking = BookingFactory.randomBooking();
        sharedBookingId = BookerApiClient.createBooking(sharedBooking).as(CreateBookingResponse.class).getBookingid();
    }

    @Test
    @Order(1)
    @DisplayName("TC-BOOK-001: Create booking with valid data returns the created booking")
    void createBookingWithValidData() {
        Booking booking = BookingFactory.randomBooking();

        Response response = BookerApiClient.createBooking(booking);

        response.then().statusCode(200);
        CreateBookingResponse created = response.as(CreateBookingResponse.class);
        assertThat(created.getBookingid()).isPositive();
        assertThat(created.getBooking()).isEqualTo(booking);
    }

    @Test
    @Order(2)
    @DisplayName("TC-BOOK-002: Get booking by id returns the matching booking data")
    void getBookingWithValidId() {
        Response response = BookerApiClient.getBooking(sharedBookingId);

        response.then().statusCode(200);
        assertThat(response.as(Booking.class)).isEqualTo(sharedBooking);
    }

    @Test
    @Order(3)
    @DisplayName("TC-BOOK-006: Get booking ids without filters returns a non-empty list")
    void getBookingIdsWithoutFilters() {
        Response response = BookerApiClient.getBookingIds(null);

        response.then().statusCode(200);
        assertThat(response.jsonPath().getList("bookingid", Integer.class)).isNotEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("TC-BOOK-004: Partial update with a valid token only changes provided fields")
    void partialUpdateBookingWithValidToken() {
        uniqueFirstname = "Flamingo" + System.currentTimeMillis();

        Response response = BookerApiClient.partialUpdateBooking(
                sharedBookingId, Map.of("firstname", uniqueFirstname), AuthSession.getToken());

        response.then().statusCode(200);
        Booking updated = response.as(Booking.class);
        assertThat(updated.getFirstname()).isEqualTo(uniqueFirstname);
        assertThat(updated.getLastname()).isEqualTo(sharedBooking.getLastname());
        assertThat(updated.getTotalprice()).isEqualTo(sharedBooking.getTotalprice());
    }

    @Test
    @Order(5)
    @DisplayName("TC-BOOK-007: Get booking ids filtered by first/last name includes the updated booking")
    void getBookingIdsFilteredByFirstNameAndLastName() {
        Response response = BookerApiClient.getBookingIds(
                Map.of("firstname", uniqueFirstname, "lastname", sharedBooking.getLastname()));

        response.then().statusCode(200);
        assertThat(response.jsonPath().getList("bookingid", Integer.class)).contains(sharedBookingId);
    }

    @Test
    @Order(6)
    @DisplayName("TC-BOOK-003: Update booking with a valid token updates all fields")
    void updateBookingWithValidTokenAndData() {
        Booking updatedBooking = BookingFactory.randomBooking();

        Response response = BookerApiClient.updateBooking(sharedBookingId, updatedBooking, AuthSession.getToken());

        response.then().statusCode(200);
        assertThat(response.as(Booking.class)).isEqualTo(updatedBooking);
    }

    @Test
    @Order(7)
    @DisplayName("TC-BOOK-005: Delete booking with a valid token removes the booking")
    void deleteBookingWithValidToken() {
        Response deleteResponse = BookerApiClient.deleteBooking(sharedBookingId, AuthSession.getToken());
        deleteResponse.then().statusCode(201);

        BookerApiClient.getBooking(sharedBookingId).then().statusCode(404);
    }
}
