package com.flamingo.qa.model.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Dates are plain ISO (yyyy-MM-dd) strings, matching the Restful Booker API contract. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDates {
    private String checkin;
    private String checkout;
}
