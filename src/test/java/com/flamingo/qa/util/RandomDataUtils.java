package com.flamingo.qa.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/** Generates randomized test data so tests don't collide on shared public test data. */
public final class RandomDataUtils {

    private static final String[] FIRST_NAMES = {"John", "Jane", "Alex", "Emily", "Chris", "Katie", "Mike", "Sara"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Brown", "Taylor", "Anderson", "Clark"};

    private RandomDataUtils() {
    }

    public static String randomFirstName() {
        return FIRST_NAMES[ThreadLocalRandom.current().nextInt(FIRST_NAMES.length)] + randomSuffix();
    }

    public static String randomLastName() {
        return LAST_NAMES[ThreadLocalRandom.current().nextInt(LAST_NAMES.length)];
    }

    public static int randomPrice() {
        return ThreadLocalRandom.current().nextInt(50, 1000);
    }

    public static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static String futureDate(int daysFromNow) {
        return LocalDate.now().plusDays(daysFromNow).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static String randomSuffix() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999));
    }
}
