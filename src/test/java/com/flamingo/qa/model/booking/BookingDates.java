package com.flamingo.qa.model.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/** Dates are serialized as ISO (yyyy-MM-dd) strings, matching the Restful Booker API contract. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDates {
    private LocalDate checkin;
    private LocalDate checkout;
}
