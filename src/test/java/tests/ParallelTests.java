package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Execution(ExecutionMode.CONCURRENT)
public class ParallelTests {
    private Playwright playwright;
    private Browser browser;

    @BeforeEach
    void setUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterEach
    void tearDown(){
        browser.close();
        playwright.close();
    }

    @Test
    public void loginPage(){
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://the-internet.herokuapp.com/login",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        assertTrue(page.title().contains("The Internet"), "Заголовок не совпадает");
        context.close();
    }

    @Test
    public void addRemoveButton(){
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://the-internet.herokuapp.com/add_remove_elements/",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.click("//button[text()='Add Element']");
        assertTrue(page.isVisible("//button[text()='Delete']"));
        context.close();
    }
}
