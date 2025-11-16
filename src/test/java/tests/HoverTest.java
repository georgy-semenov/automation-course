package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.*;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HoverTest {
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
    void contextClose() {
        context.close();
    }

    @Test
    public void testHoverProfiles(){
        page.navigate("https://the-internet.herokuapp.com/hovers", new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        Locator figures = page.locator(".figure");
        int count = figures.count();

        for (int i = 0; i < count; i++) {
            Locator figure = figures.nth(i);
            figure.hover();

            Locator profileLink = figure.locator("text=View profile");
            profileLink.waitFor();
            assertTrue(profileLink.isVisible());

            profileLink.click();

            page.waitForURL(Pattern.compile(".*/users/\\d+"));

            page.goBack();

        }
    }

}
