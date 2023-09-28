package org.example;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class StartupLoggingTest {
    private static final Logger LOGGER = Logger.getLogger(StartupLoggingTest.class);

    @Test
    void run() {
        LOGGER.info("message from test");
    }
}
