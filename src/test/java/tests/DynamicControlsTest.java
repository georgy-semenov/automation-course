package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicControlsTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @Test
    void testDynamicCheckbox() {
        String checkBoxLocator = "//input[@type='checkbox']";
        String messageLocator = "//p[@id='message']";
        String buttonLocator = "button[onclick='swapCheckbox()']";
        page.navigate("https://the-internet.herokuapp.com/dynamic_controls", new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        assertTrue(page.locator(checkBoxLocator).isVisible());

        page.click(buttonLocator);
        page.waitForSelector(messageLocator);
        assertTrue(page.locator(messageLocator).isVisible());

        page.click(buttonLocator);
        page.waitForSelector(messageLocator);
        assertTrue(page.locator(checkBoxLocator).isVisible());
    }

    @AfterEach
    void tearDown() {
        page.close();
        browser.close();
        playwright.close();
    }
}
