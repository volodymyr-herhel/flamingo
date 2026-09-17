package com.flamingo.qa.model.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private String firstname;
    private String lastname;
    private double totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;
}
