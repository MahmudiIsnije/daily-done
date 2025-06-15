package com.dailydone.dailydone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DailydoneApplicationTests {

    @Test
    void contextLoads() {
        // Einfacher Test - Spring Context lädt
        System.out.println("✅ Spring Context erfolgreich geladen!");
    }
}