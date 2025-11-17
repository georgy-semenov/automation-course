package tests;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import example.StatusConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusCodeTest {
    private StatusConfig config;
    private Page page;

    @BeforeAll
    public static void setProperty(){
        System.setProperty("env", "prod");
    }
    @BeforeEach
    public void setup() {
        config = ConfigFactory.create(StatusConfig.class, System.getenv());
        page = Playwright.create().chromium().launch().newPage();
    }

    @Test
    public void test200() {
        int[] statusCode = {0};

        page.onResponse(response -> {
            if (response.url().endsWith("/status_codes/200")) {
                statusCode[0] = response.status();
            }
        });

        page.navigate(config.baseUrl() + "/status_codes/200");
        assertEquals(200, statusCode[0]);
    }

    @AfterEach
    public void teardown() {
        page.close();
    }
}
