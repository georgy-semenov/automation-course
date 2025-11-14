package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MobileDynamicControlsTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();

        // Настройка параметров iPad Pro 11
        Browser.NewContextOptions deviceOptions = new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)")
                .setViewportSize(834, 1194)
                .setDeviceScaleFactor(2)
                .setIsMobile(true)
                .setHasTouch(true);

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(deviceOptions);
        page = context.newPage();
    }

    @Test
    void testInputEnabling() {
        page.navigate("https://the-internet.herokuapp.com/dynamic_controls",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        step("Нажимаем на кнопку Enable", ()->page.click("(//button[@type='button'])[2]"));
        step("Ожидаем отображение надписи It's enabled!", ()->page.locator("//p[@id='message']").waitFor());
        step("Проверка отображения надписи It's enabled!", ()->assertTrue(page.locator("//p[@id='message']").isVisible()));
        step("Проверка что поле ввода стало активным", ()->{
            assertTrue(page.locator("//input[@type='text']").isEnabled(), "Поле ввода должно быть активным");
        });
    }

    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }

}
