package com.flamingo.qa.factory;

import com.flamingo.qa.model.booking.AdditionalNeeds;
import com.flamingo.qa.model.booking.Booking;
import com.flamingo.qa.model.booking.BookingDates;
import com.flamingo.qa.util.RandomDataUtils;

/** Produces ready-to-use {@link Booking} test data for common scenarios. */
public final class BookingFactory {

    private BookingFactory() {
    }

    public static Booking randomBooking() {
        return Booking.builder()
                .firstname(RandomDataUtils.randomFirstName())
                .lastname(RandomDataUtils.randomLastName())
                .totalprice(RandomDataUtils.randomPrice())
                .depositpaid(RandomDataUtils.randomBoolean())
                .bookingdates(BookingDates.builder()
                        .checkin(RandomDataUtils.futureDate(1))
                        .checkout(RandomDataUtils.futureDate(5))
                        .build())
                .additionalneeds(AdditionalNeeds.BREAKFAST.getValue())
                .build();
    }

    public static Booking randomBookingWithNames(String firstname, String lastname) {
        Booking booking = randomBooking();
        booking.setFirstname(firstname);
        booking.setLastname(lastname);
        return booking;
    }

    public static Booking randomBookingWithDates(String checkin, String checkout) {
        Booking booking = randomBooking();
        booking.setBookingdates(BookingDates.builder().checkin(checkin).checkout(checkout).build());
        return booking;
    }
}
