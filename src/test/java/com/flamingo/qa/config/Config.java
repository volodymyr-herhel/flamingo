package com.flamingo.qa.config;

/** Central place for environment-configurable endpoints (override via -D system properties). */
public final class Config {

    public static final String BOOKER_BASE_URL =
            System.getProperty("booker.baseUrl", "https://restful-booker.herokuapp.com");

    public static final String HYGRAPH_VIDEO_ENDPOINT =
            System.getProperty("hygraph.endpoint",
                    "https://us-east-1-shared-usea1-02.cdn.hygraph.com/content/clpvcopq3aavs01usft1idkgj/master");

    private Config() {
    }
}
