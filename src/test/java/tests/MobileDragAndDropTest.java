package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MobileDragAndDropTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();

        // Ручная настройка параметров Samsung Galaxy S22 Ultra
        Browser.NewContextOptions deviceOptions = new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (Linux; Android 12; SM-S908B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Mobile Safari/537.36")
                .setViewportSize(384, 873)  // Разрешение экрана
                .setDeviceScaleFactor(3.5)
                .setIsMobile(true)
                .setHasTouch(true);

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(deviceOptions);
        page = context.newPage();
    }

    @Test
    public void testDragAndDropMobile(){
        page.navigate("https://the-internet.herokuapp.com/drag_and_drop",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        Locator form_A = page.locator("#column-a");
        Locator form_B = page.locator("#column-b");

        assertEquals("A", form_A.textContent());
        assertEquals("B", form_B.textContent());

        page.evaluate("() => {\n" +
                "  const dataTransfer = new DataTransfer();\n" +
                "  const dragStartEvent = new DragEvent('dragstart', { dataTransfer });\n" +
                "  const dropEvent = new DragEvent('drop', { dataTransfer, bubbles: true });\n" +
                "\n" +
                "  document.querySelector('#column-a').dispatchEvent(dragStartEvent);\n" +
                "\n" +
                "  document.querySelector('#column-b').dispatchEvent(dropEvent);\n" +
                "}");

        page.waitForTimeout(1000);
        assertEquals("A", form_B.textContent());
    }

    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }

}
