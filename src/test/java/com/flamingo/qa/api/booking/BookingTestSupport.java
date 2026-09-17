package com.flamingo.qa.api.booking;

import com.flamingo.qa.client.AuthSession;
import com.flamingo.qa.client.BookerApiClient;
import com.flamingo.qa.model.booking.Booking;
import com.flamingo.qa.model.booking.CreateBookingResponse;
import org.junit.jupiter.api.AfterEach;

/**
 * Shared per-test booking lifecycle: {@link #createBooking(Booking)} creates a booking and
 * tracks its id, {@link #cleanupBooking()} deletes it afterwards - so booking test classes don't
 * depend on each other and don't leave data behind on the shared public API.
 */
abstract class BookingTestSupport {

    protected Integer bookingId;

    @AfterEach
    void cleanupBooking() {
        if (bookingId != null) {
            BookerApiClient.deleteBooking(bookingId, AuthSession.getToken());
        }
    }

    protected int createBooking(Booking booking) {
        bookingId = BookerApiClient.createBooking(booking).as(CreateBookingResponse.class).getBookingid();
        return bookingId;
    }
}
