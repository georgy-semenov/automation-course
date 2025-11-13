package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicLoadingTraceTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @Test
    void testDynamicLoadingWithTrace() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();

        // Настройка трассировки
        context.tracing().start(new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
                        .setTitle("My Java Trace"));
                page = context.newPage();

        // Шаги теста
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.click("button"); // Клик на "Start"
        assertTrue(page.waitForSelector("#loading").isVisible(), "Бар загрузки не отображается");//Ожидаем начала бара с загрузкой
        // Ожидание появления текста
        page.locator("#finish").waitFor();
        assertTrue(page.locator("#finish").isVisible(), "Элемент не отображается");

        // Сохранение трассировки
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace-dynamic-loading.zip")));
    }

    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}
