package com.example.lab.support;

import java.time.Duration;

/** All runtime settings in one place. Values come from -D flags (see pom.xml defaults). */
public final class Config {
    private Config() {}

    public static final String BROWSER  = System.getProperty("browser", "chrome").toLowerCase();
    public static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "true"));
    public static final String BASE_URL = System.getProperty("baseUrl", "https://www.saucedemo.com");
    public static final String RUN_ID   = System.getProperty("runId", "local");
    public static final Duration TIMEOUT = Duration.ofSeconds(10);

    // Public demo credentials published by Sauce Labs for saucedemo.com
    public static final String PASSWORD = "secret_sauce";
    public static final String STANDARD_USER = "standard_user";
    public static final String LOCKED_USER = "locked_out_user";
    public static final String GLITCH_USER = "performance_glitch_user";
}
