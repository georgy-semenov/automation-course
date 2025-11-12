package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class TestHomePageVisual {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    @BeforeAll
    public static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(500));
    }

    @AfterAll
    public static void closeBrowser() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setup() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void attachScreenshotOnFailure() {
        context.close();
    }

    @Test
    void testHomePageVisual() throws IOException {
        page.navigate("https://the-internet.herokuapp.com");


        Path actual = Paths.get("target/screenshots/actual.png");
        Path expected = Paths.get("src/test/resources/screenshots/expected.png");
        Path diff = Paths.get("target/screenshots/diff.png");

        Files.createDirectories(actual.getParent());
        page.screenshot(new Page.ScreenshotOptions().setPath(actual));

        if (!Files.exists(expected)) {
            throw new IOException("Эталонный скриншот не найден: " + expected);
        }

        long mismatch = Files.mismatch(actual, expected);

        if (mismatch != -1) {
            Files.copy(actual, diff, StandardCopyOption.REPLACE_EXISTING);
            throw new AssertionError(
                    "Скриншоты не совпадают! Визуальные различия обнаружены. Diff сохранён: " + diff.toAbsolutePath()
            );
        }
    }
}
