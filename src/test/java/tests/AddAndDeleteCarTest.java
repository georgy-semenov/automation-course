package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddAndDeleteCarTest implements TestWatcher {
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
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))
                .setRecordVideoSize(1280, 720));
        page = context.newPage();
    }
    @AfterEach
    void attachScreenshotOnFailure() {
        context.close();
    }
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        byte[] screenshot = page.screenshot();
        Allure.addAttachment(
                "Screenshot on Failure",
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png"
        );
    }

    @Test
    public void carTest() {
        page.navigate("https://the-internet.herokuapp.com/add_remove_elements/", new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        // Добавление товара
        page.click("//button[text()='Add Element']");
        page.locator("//body").screenshot(new Locator.ScreenshotOptions()
                .setPath(getTimestampPath("cart_after_add.png")));

        // Удаление товара
        page.click("//button[text()='Delete']");
        page.locator("//body").screenshot(new Locator.ScreenshotOptions()
                .setPath(getTimestampPath("cart_after_remove.png")));
    }

    private Path getTimestampPath(String filename) {
        Path screenshotsDir = Paths.get("screenshots");
        if (!Files.exists(screenshotsDir)) {
            try {
                Files.createDirectory(screenshotsDir);
            } catch (IOException e) {
                throw new RuntimeException("Не удалось создать директорию: " + screenshotsDir, e);
            }
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        String newFilename = timestamp + "_" + filename;

        return screenshotsDir.resolve(newFilename);
    }
}
