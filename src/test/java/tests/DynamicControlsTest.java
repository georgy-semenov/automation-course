package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.DynamicControlsPage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicControlsTest {
    private TestContext context;
    private DynamicControlsPage dynamicControlsPage;

    @BeforeEach
    public void setup() {
        context = new TestContext();
        dynamicControlsPage = new DynamicControlsPage(context.getPage());
        context.getPage().navigate("https://the-internet.herokuapp.com/dynamic_controls",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }

    @Test
    public void testCheckboxRemoval() {
        dynamicControlsPage.clickRemoveButton();
        dynamicControlsPage.waitForElement();
        assertFalse(dynamicControlsPage.isCheckboxVisible());
    }

    @AfterEach
    public void teardown() {
        context.getPage().close();
    }
}
