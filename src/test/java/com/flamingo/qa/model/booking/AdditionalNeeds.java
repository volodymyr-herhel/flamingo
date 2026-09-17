package com.flamingo.qa.model.booking;

/** Enumerates the "additionalneeds" values accepted by the Restful Booker API. */
public enum AdditionalNeeds {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    WIFI("Wifi"),
    PARKING("Parking"),
    NONE("");

    private final String value;

    AdditionalNeeds(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
